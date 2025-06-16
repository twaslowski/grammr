module "k3s_master" {
  source  = "terraform-aws-modules/ec2-instance/aws"
  version = "~> 5.8"

  name = local.instance_name

  monitoring                  = true
  create_iam_instance_profile = true
  iam_role_description        = "IAM role for EC2 instance"
  iam_role_policies = {
    AmazonSSMManagedInstanceCore = "arn:aws:iam::aws:policy/AmazonSSMManagedInstanceCore"
  }

  ami                    = data.aws_ami.amazon_linux_2023_arm64.id
  instance_type          = "t4g.small"
  subnet_id              = data.aws_subnets.default.ids[0]
  vpc_security_group_ids = [data.aws_security_group.default.id]

  root_block_device = [
    {
      volume_size           = 20
      volume_type           = "gp3"
      delete_on_termination = false
      encrypted             = true
    }
  ]

  user_data_replace_on_change = true
  user_data                   = <<EOF
    #!/bin/bash
    set -e pipefail

    curl -fsSL https://tailscale.com/install.sh | sh
    sudo tailscale up --auth-key="${var.tailscale_auth_key}" --hostname=ec2-k3s-master

    export INSTALL_K3S_SKIP_SELINUX_RPM=true
    export INSTALL_K3S_EXEC="server"
    curl -sfL https://get.k3s.io | sh -s - --write-kubeconfig-mode 644
  EOF
}
