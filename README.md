<div align="center">

# Hackathon - Process

![GitHub Release Date](https://img.shields.io/badge/Release%20Date-Abril%202025-yellowgreen)
![](https://img.shields.io/badge/Status-Desenvolvido-brightgreen)
<br>
![](https://img.shields.io/badge/Version-%20v1.0.0-brightgreen)
</div>

## 💻 Descrição

Este projeto implementa um processador para transformação de videos em frames na arquitetura de microserviços da Hackathon Video.

## 🛠 Tecnologias Utilizadas

![Java](https://img.shields.io/badge/java_21-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring](https://img.shields.io/badge/spring_3-%236DB33F.svg?style=for-the-badge&logo=springboot&logoColor=white)
![Maven](https://img.shields.io/badge/Apache%20Maven-C71A36.svg?style=for-the-badge&logo=Apache-Maven&logoColor=white)
![JUnit5](https://img.shields.io/badge/JUnit5-25A162.svg?style=for-the-badge&logo=JUnit5&logoColor=white)
![Mockito](https://img.shields.io/badge/Mockito-53AC56.svg?style=for-the-badge&logo=Minetest&logoColor=white)

![JCodec](https://img.shields.io/badge/JCodec-0B4EA2.svg?style=for-the-badge&logo=CodeChef&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white)
![AWS S3](https://img.shields.io/badge/Amazon%20S3-569A31.svg?style=for-the-badge&logo=Amazon-S3&logoColor=white)
![AWS SQS](https://img.shields.io/badge/Amazon%20SQS-FF4F8B.svg?style=for-the-badge&logo=Amazon-SQS&logoColor=white)

![Sonar](https://img.shields.io/badge/Sonar-FD3456.svg?style=for-the-badge&logo=Sonar&logoColor=white)
![GitHub Actions](https://img.shields.io/badge/GitHub%20Actions-2088FF.svg?style=for-the-badge&logo=GitHub-Actions&logoColor=white)
![Terraform](https://img.shields.io/badge/Terraform-7B42BC?style=for-the-badge&logo=terraform&logoColor=white)
![OpenTelemetry](https://img.shields.io/badge/OpenTelemetry-000000.svg?style=for-the-badge&logo=OpenTelemetry&logoColor=white)

## 💫 Arquitetura

O projeto adota a **Clean Architecture**, garantindo flexibilidade, testabilidade e manutenção escalável.

## ⚙️ Configuração

### Pré-requisitos

### Desenvolvimento

- **[Java 21](https://docs.oracle.com/en/java/javase/21/)**: Documentação oficial do Java 21.
- **[Maven 3.6.3+](https://maven.apache.org/)**: Site oficial do Maven.
- **[Docker](https://www.docker.com/)**: Site oficial do Docker.
- **[Docker Compose](https://docs.docker.com/compose/)**: Documentação oficial do Docker Compose.
- **[Sonarqube](https://www.sonarsource.com/products/sonarqube/)**: Site oficial do Sonarqube.
- **[Kubernetes](https://kubernetes.io/pt-br/docs/home/)**: Documentação oficial do Kubernetes.
- **[Terraform](https://www.terraform.io/)**: Site oficial do Terraform.
- **[AWS](https://aws.amazon.com/pt/)**: Site oficial da AWS.
- **[JCodec](http://jcodec.org/)**: Documentação da biblioteca JCodec.

### 🚀 Execução

### Subindo a aplicação com Docker Compose

1. Executar o comando:

```sh
docker compose up
```

### Subindo o MS Process com Terraform
Caso deseje subir a MS Process com o Terraform, basta seguir os seguintes passos:

1. Certificar que o Terraform esteja instalado executando o comando `terraform --version`;
2. Certificar que o `aws cli` está instalado e configurado com as credenciais da sua conta AWS;
3. Acessar a pasta `terraform` que contém os arquivos que irão criar o MS Process;
4. Inicializar o Terraform no projeto `terraform init`;
5. Verificar que o script do Terraform é valido rodando o comando `terraform validate`;
6. Executar o comando `terraform plan` para executar o planejamento da execução/implementação;
7. Executar o comando `terraform apply` para criar o MS Process;
8. Após a execução do Terraform finalizar, verificar se o Process subiu corretamente na AWS;

## ✅ Cobertura de Testes

### Testes Unitarios
![unit-test](./assets/unit_test_process.png)

### Scan do Sonar
![Sonar](./assets/sonar_process.png)

### Verificação de vulnerabilidades com Check Dependency OWASP
![Check_Dependency](./assets/check_dependency.png)