resource "kubernetes_namespace" "process_namespace" {
  metadata {
    name = "process"
  }
}
