# Deploying to AWS (cheapest path)

This guide deploys the **server + database** to a single AWS EC2 free-tier
instance using `docker-compose.prod.yml` and verifies it. The Swing client stays on each player's
machine and points at the deployed server via `CHESS_SERVER_URL` / `CHESS_API_KEY`.

The whole thing is reproducible: spinning it back up is the same handful of
commands below.

---

## Step 0: Set a billing alarm FIRST

Billing -> Budgets -> Create budget -> **$1 monthly cost alert** to your email.
Free tier expires after 12 months (a `t3.micro` is then ~$7-9/mo), so this is
your safety net against a forgotten resource.

## Step 1: Launch an EC2 instance

EC2 -> Launch instance:

- **AMI:** Amazon Linux 2023
- **Type:** `t3.micro` (or `t2.micro` - whichever is _Free tier eligible_ in your region)
- **Key pair:** create one, download the `.pem`
- **Storage:** 30 GiB gp3 (free-tier ceiling); leave **"Delete on termination" = Yes**
- **Security group:**
  - SSH (22) - source **My IP** only
  - Custom TCP (8080) - source **Anywhere** (the API)
  - Do NOT open 5432/5434 (Postgres) or 8081 (Adminer)

## Step 2: SSH in and install Docker

```bash
ssh -i "chess-key.pem" ec2-user@public-ip

sudo dnf update -y
sudo dnf install -y docker git
sudo systemctl enable --now docker
sudo usermod -aG docker ec2-user
sudo mkdir -p /usr/local/lib/docker/cli-plugins
sudo curl -SL https://github.com/docker/compose/releases/latest/download/docker-compose-linux-x86_64 \
  -o /usr/local/lib/docker/cli-plugins/docker-compose
sudo chmod +x /usr/local/lib/docker/cli-plugins/docker-compose
```

Log out/in (or run `newgrp docker`) so the docker group applies.

## Step 3: Add swap (1 GB RAM is tight)

```bash
sudo dd if=/dev/zero of=/swapfile bs=1M count=2048
sudo chmod 600 /swapfile
sudo mkswap /swapfile && sudo swapon /swapfile
echo '/swapfile none swap sw 0 0' | sudo tee -a /etc/fstab
```

## Step 4: Get the code and launch

```bash
git clone https://github.com/Ian8912/ChessOOP.git && cd ChessOOP

export POSTGRES_PASSWORD='choose-a-strong-one'
export APP_API_KEY="$(openssl rand -hex 24)"
echo "API KEY = $APP_API_KEY"   # save this - the client needs it

docker compose -f docker-compose.prod.yml up --build -d
```

> If the Gradle build runs out of memory on the 1 GB box, build the image
> locally instead, push it to a free Docker Hub repo, and `docker pull` it on
> EC2 (replace the `build:` block with `image:`). Swap usually gets it through,
> just slowly.

## Step 5: Verify (and capture proof for interviews)

```bash
curl http://localhost:8080/actuator/health                 # -> {"status":"UP"}
curl "http://localhost:8080/api/v1/leaderboard?limit=10"   # -> JSON array
```

From your laptop: open `http://<ec2-public-ip>:8080/actuator/health`.
Point the client at it and play a game so the leaderboard has data:

```powershell
$env:CHESS_SERVER_URL="http://<ec2-public-ip>:8080"; $env:CHESS_API_KEY="<the key>"
gradlew.bat :client:run
```

**Take screenshots now** (health check + leaderboard JSON, EC2 console showing
the running instance). These are your proof after teardown.

---

## Step 6: Termination Steps

Do these in order and confirm each:

1. **Terminate the instance:** EC2 -> Instances -> select -> Instance state ->
   **Terminate**. This stops all compute charges.
2. **Confirm the EBS volume is gone:** EC2 -> Volumes. If "Delete on termination"
   was Yes (Step 1), it's already deleted. If any volume lingers in `available`
   state, **delete it** - detached volumes still bill.
3. **Release any Elastic IP:** EC2 -> Elastic IPs. If you allocated one, **release**
   it. An unattached Elastic IP is the #1 surprise charge.
4. **Delete snapshots:** EC2 -> Snapshots - delete any you created.
5. **(Optional) delete the key pair and custom security group** - these are free,
   but clean them up if you like.
6. **Confirm in Billing:** Billing -> Bills the next day should show ~$0. The $1
   budget alarm backstops anything you missed.

Nothing in this stack uses RDS, load balancers, or NAT gateways (the usual money
pits), so once the instance, volume, and any Elastic IP are gone, billing is $0.

---

## Re-deploying later

Repeat Steps 1-4. The app is stateless and the DB starts empty (local `pgdata`
does not transfer), so a fresh deploy is clean every time.
