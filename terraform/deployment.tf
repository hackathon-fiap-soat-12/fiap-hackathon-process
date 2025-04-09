resource "kubernetes_deployment" "process_deployment" {
  metadata {
    name      = "fiap-hackathon-process-app"
    namespace = kubernetes_namespace.process_namespace.metadata[0].name
    labels = {
      app = "fiap-hackathon-process-app"
    }
  }

  spec {
    replicas = 1

    selector {
      match_labels = {
        app = "fiap-hackathon-process-app"
      }
    }

    template {
      metadata {
        labels = {
          app = "fiap-hackathon-process-app"
        }
      }

      spec {
        container {
          image             = data.aws_ecr_image.latest_image.image_uri
          name              = "fiap-hackathon-process-app"
          image_pull_policy = "Always"

          resources {
            limits = {
              cpu    = "500m"
              memory = "1Gi"
            }
            requests = {
              cpu    = "250m"
              memory = "512Mi"
            }
          }

          liveness_probe {
            http_get {
              path = "/actuator/health"
              port = var.server_port
            }
            initial_delay_seconds = 60
            period_seconds        = 30
            timeout_seconds       = 5
            failure_threshold     = 3
          }

          readiness_probe {
            http_get {
              path = "/actuator/health"
              port = var.server_port
            }
            initial_delay_seconds = 60
            period_seconds        = 10
            timeout_seconds       = 3
            failure_threshold     = 1
          }

          env {
            name  = "SPRING_PROFILES_ACTIVE"
            value = "default"
          }

          env {
            name  = "SQS_QUEUE_PROCESS_VIDEO_LISTENER"
            value = "process-video-queue"
          }

          env {
            name  = "SQS_QUEUE_PROCESS_VIDEO_PRODUCER"
            value = data.aws_sqs_queue.video_update_queue.url
          }

          env {
            name = "AWS_ACCESS_KEY_ID"
            value_from {
              secret_key_ref {
                name = "fiap-hackathon-process-secret"
                key  = "aws_access_key_id"
              }
            }
          }

          env {
            name = "AWS_SECRET_ACCESS_KEY"
            value_from {
              secret_key_ref {
                name = "fiap-hackathon-process-secret"
                key  = "aws_secret_access_key"
              }
            }
          }

          env {
            name = "AWS_SESSION_TOKEN"
            value_from {
              secret_key_ref {
                name = "fiap-hackathon-process-secret"
                key  = "aws_session_token"
              }
            }
          }

          env {
            name  = "AWS_REGION"
            value = "us-east-1"
          }

          env {
            name = "OTEL_SERVICE_NAME"
            value = "process-app"
          }

          env {
            name = "OTEL_EXPORTER_OTLP_ENDPOINT"
            value = var.otlp_endpoint
          }

          env {
            name = "OTEL_EXPORTER_OTLP_PROTOCOL"
            value = "grpc"
          }

          env {
            name = "OTEL_TRACES_EXPORTER"
            value = "otlp"
          }

          env {
            name  = "OTEL_METRICS_EXPORTER"
            value = "none"
          }

          env {
            name  = "OTEL_LOGS_EXPORTER"
            value = "otlp"
          }

          env {
            name = "OTEL_JAVAAGENT_DEBUG"
            value = "true"
          }

          env {
            name = "OTEL_JAVA_DISABLED_EXPORTERS"
            value = "logging"
          }
        }
      }
    }
  }

  timeouts {
    create = "4m"
    update = "4m"
    delete = "4m"
  }

  depends_on = [kubernetes_secret.process_secret]
}
