data "aws_eks_cluster" "eks_cluster" {
  name = var.eks_cluster_name
}

data "aws_eks_cluster_auth" "eks_cluster" {
  name = data.aws_eks_cluster.eks_cluster.name
}

data "aws_ecr_repository" "ecr_repo" {
  name = var.ecr_repository_name
}

data "aws_ecr_image" "latest_image" {
  repository_name = data.aws_ecr_repository.ecr_repo.name
  image_tag       = "latest"
}

data "aws_sqs_queue" "payment_order_create_queue" {
  name = "payment-order-create-queue"
}

data "aws_sqs_queue" "order-status-update-queue" {
  name = "order-status-update-queue"
}
