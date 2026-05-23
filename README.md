# 🎫 Projeto Marketplace

<a href="https://skillicons.dev">
    <img src="https://skillicons.dev/icons?i=java,spring,gradle&theme=dark" />
</a>

##### Um sistema de marketplace para venda de ingressos de eventos, desenvolvido com **Spring Boot 4.0.5** usando uma arquitetura modular baseada em domínios. O projeto é estruturado em módulos independentes (Registration, Catalog e Ticketing), cada um com sua própria base de dados e responsabilidades específicas.

## 📋 Visão Geral

O Marketplace é uma aplicação que permite:

- **Cadastro de Clientes (Registration)**: Gerenciar informações de clientes na plataforma
- **Catálogo de Eventos (Catalog)**: Visualizar e gerenciar eventos disponíveis com metadados enriquecidos
- **Seleção de Ingressos (Ticketing)**: Selecionar e reservar assento com controle de concorrência

### Tecnologias Principais

- **Java 25** com Spring Boot 4.0.5
- **Banco de Dados**:
  - MySQL (Registration e Catalog)
  - PostgreSQL (Ticketing)
  - MongoDB (Metadados do Catalog)
- **Cache**: Redis (Catalog e Ticketing)
- **Build**: Gradle com Docker Compose

## 🏗️ Arquitetura e Estrutura de Pastas

### Pasta Principal: `src/main/java/marcio/marketplace`

O projeto segue a arquitetura **modular com separação por domínios**, onde cada módulo é uma unidade independente com suas responsabilidades claras.

```
marketplace/
├── MarketplaceApplication.java        # Classe principal da aplicação
├── catalog/                           # Módulo de Catálogo de Eventos
├── registration/                      # Módulo de Registro de Clientes
├── ticketing/                         # Módulo de Compra de Ingressos
└── common/                            # Código compartilhado entre módulos
```

---

## 📦 Módulos Detalhados

### 1. 📚 **CATALOG** (Catálogo de Eventos)

**Responsabilidade**: Gerenciar o catálogo de eventos e suas informações

**Database**: 
- MySQL (porta 3308) - dados estruturados
- MongoDB (porta 27018) - metadados dos eventos
- Redis (porta 6380) - cache

**Estrutura**:

```
catalog/
├── application/                       # Casos de uso e DTOs
│   ├── BrowseShowcaseUseCase.java    # Use case para navegar no catálogo
│   ├── EventEnricher.java            # Enriquece eventos com metadados
│   └── dto/
│       └── EventOutput.java          # DTO para resposta de eventos
│
├── domain/                            # Entidades e lógica de negócio
│   ├── Event.java                    # Entidade: Evento
│   ├── EventId.java                  # Value Object: ID do evento
│   ├── EventMetadata.java            # Entidade: Metadados do evento
│   ├── EventMetadataRepository.java   # Interface do repositório
│   ├── EventRepository.java           # Interface do repositório
│   ├── Seat.java                     # Entidade: Assento
│   ├── SeatId.java                   # Value Object: ID do assento
│   ├── Sector.java                   # Entidade: Setor de assentos
│   └── SectorId.java                 # Value Object: ID do setor
│
└── infrastructure/                    # Implementações técnicas
    ├── event/                         # Event listeners para integração
    │   ├── EventListener.java
    │   └── EventMetadataEventListener.java
    ├── http/
    │   └── ShowcaseController.java   # REST Controller
    └── persistence/                   # Persistência em BD
        ├── entity/                    # Entidades JPA/MongoDB
        │   ├── Event.java
        │   └── EventMetadata.java
        └── repository/                # Implementações de repositórios
            ├── EventEntityRepository.java
            ├── EventMetadataEntityRepository.java
            ├── JpaEventRepository.java
            └── MongoEventMetadataRepository.java
```

**Fluxo de Dados**:
1. Controlador recebe requisição de catálogo
2. Use case `BrowseShowcaseUseCase` recupera eventos
3. `EventEnricher` enriquece com metadados do MongoDB
4. Dados são cacheados em Redis
5. Resposta retornada como `EventOutput`

---

### 2. 👤 **REGISTRATION** (Registro de Clientes)

**Responsabilidade**: Gerenciar o registro e informações de clientes

**Database**: 
- MySQL (porta 3307) - dados de clientes

**Estrutura**:

```
registration/
├── application/                       # Casos de uso
│
├── domain/                            # Entidades de negócio
│   ├── Customer.java                 # Entidade: Cliente
│   ├── CustomerId.java               # Value Object: ID do cliente
│   └── CustomeRepository.java         # Interface do repositório (typo: CustomeRepository)
│
└── infrastructure/                    # Implementações técnicas
    ├── RegistrationConfiguration.java # Configuração do módulo
    ├── event/
    │   └── CustomerEventHandler.java  # Manipulador de eventos
    └── persistence/
        ├── entity/
        │   ├── Address.java           # Entidade: Endereço
        │   └── ...                    # Outras entidades
        └── repository/                # Repositórios JPA
```

**Responsabilidades**:
- Criar e manter registros de clientes
- Gerenciar endereços e informações de contato
- Publicar eventos quando clientes são criados (usado por outros módulos)

---

### 3. 🎟️ **TICKETING** (Compra de Ingressos)

**Responsabilidade**: Gerenciar seleção e reserva de ingressos (assentos)

**Database**: 
- PostgreSQL (porta 5433) - dados de transações críticas
- Redis (porta 6381) - controle de concorrência/locks

**Estrutura**:

```
ticketing/
├── application/                       # Casos de uso
│   ├── CreateCustomerUseCase.java    # Use case: Criar cliente no ticketing
│   ├── CreateEventUseCase.java       # Use case: Criar evento no ticketing
│   └── SelectSeatUseCase.java        # Use case: Selecionar assento
│
├── domain/                            # Entidades críticas
│   ├── Customer.java                 # Entidade: Cliente
│   ├── CustomerId.java               # Value Object: ID do cliente
│   ├── CustomerRepository.java        # Interface do repositório
│   ├── Event.java                    # Entidade: Evento
│   ├── EventId.java                  # Value Object: ID do evento
│   ├── EventRepository.java           # Interface do repositório
│   ├── Seat.java                     # Entidade: Assento
│   ├── SeatId.java                   # Value Object: ID do assento
│   ├── SeatAlreadyReservedException.java  # Exceção específica
│   ├── SeatNotFoundException.java     # Exceção específica
│   ├── Sector.java                   # Entidade: Setor
│   └── SectorId.java                 # Value Object: ID do setor
│
└── infrastructure/                    # Implementações técnicas
    ├── TicketingConfiguration.java   # Configuração do módulo
    ├── event/
    │   └── TicketingEventListener.java # Listener para eventos de entrada
    ├── http/
    │   ├── SeatSelectionController.java # REST Controller
    │   └── request/                   # Request DTOs
    └── persistence/
        ├── entity/                    # Entidades JPA
        └── repository/                # Repositórios com locks
```

**Características Críticas**:
- Usa **locks distribuídos (Redis)** para evitar reserva duplicada de assentos
- Integração com eventos do módulo de Catalog
- Transações críticas em PostgreSQL para garantir integridade

---

### 4. 🔄 **COMMON** (Código Compartilhado)

**Responsabilidade**: Código reutilizável entre módulos

**Estrutura**:

```
common/
└── infrastructure/
    └── event/
        └── dto/
            ├── CustomerCreated.java       # Evento: Cliente criado
            └── EventUpdated.java          # Evento: Evento atualizado
```

**Propósito**: Definir estruturas de eventos que são publicadas por um módulo e consumidas por outro, permitindo comunicação assíncrona.

---

## 🗄️ Banco de Dados

O projeto utiliza **3 bancos de dados diferentes** para cada módulo:

| Módulo | BD Principal | BD Secundária | Cache | Porta |
|--------|-------------|----------------|-------|-------|
| **Registration** | MySQL | - | - | 3307 |
| **Catalog** | MySQL | MongoDB | Redis | 3308, 27018, 6380 |
| **Ticketing** | PostgreSQL | - | Redis | 5433, 6381 |

### Configurações (application.properties)

- **Registration DB**: `jdbc:mysql://localhost:3307/registration`
- **Catalog DB**: `jdbc:mysql://localhost:3308/catalog`
- **Catalog Metadata**: MongoDB na porta 27018
- **Catalog Cache**: Redis porta 6380
- **Ticketing DB**: `jdbc:postgresql://localhost:5433/ticketing`
- **Ticketing Locking**: Redis porta 6381

---

## 🚀 Como Executar

### Pré-requisitos

- Java 25+
- Docker e Docker Compose
- Git

### 1. Iniciar os Serviços de Banco de Dados

```bash
docker-compose up -d
```

Isso iniciará:
- MySQL (Registration)
- MySQL (Catalog)
- MongoDB (Metadados do Catalog)
- Redis (Cache do Catalog)
- PostgreSQL (Ticketing)
- Redis (Locks do Ticketing)

### 2. Compilar e Executar a Aplicação

```bash
./gradlew build
./gradlew bootRun
```

Ou no Windows:
```bash
gradlew.bat build
gradlew.bat bootRun
```

### 3. Acessar a Aplicação

- **API Principal**: `http://localhost:8080`
- **Health Check**: `http://localhost:8080/actuator/health`
- **Dados REST**: `http://localhost:8080/data-rest-explorer`

---

## 🏛️ Padrões e Conceitos

### Domain-Driven Design (DDD)

O projeto segue os princípios do DDD:

- **Entidades**: `Event`, `Customer`, `Seat`, `Sector`
- **Value Objects**: `EventId`, `CustomerId`, `SeatId`, `SectorId`
- **Repositórios**: Interfaces de acesso aos dados em cada domínio
- **Use Cases**: Casos de uso específicos de cada módulo
- **Event Sourcing**: Comunicação entre módulos via eventos

### Separação de Responsabilidades

```
Domain Layer       → Lógica de Negócio Pura
Application Layer  → Use Cases e Orquestração
Infrastructure     → BD, HTTP, Cache, etc
```

### Comunicação Entre Módulos

Os módulos se comunicam através de **eventos de domínio**:

1. Um módulo publica um evento (ex: `CustomerCreated`)
2. Outro módulo escuta e reage (ex: `TicketingEventListener`)
3. Isso mantém os módulos desacoplados e independentes

---

## 🔐 Recursos de Segurança

- **Virtual Threads**: Habilitado no Spring (`spring.threads.virtual.enabled=true`) para melhor performance
- **Validação**: Spring Validation em todas as entradas
- **Async Processing**: Habilitado com `@EnableAsync` para operações não-bloqueantes
- **Caching**: Habilitado com `@EnableCaching` para otimizar consultas
- **Lock Distribuído**: Redis utilizado para locks no Ticketing, evitando race conditions

---

## 📝 Configurações Principais

| Propriedade | Valor | Descrição |
|-------------|-------|-----------|
| `spring.threads.virtual.enabled` | true | Ativa Virtual Threads do Java 21+ |
| `spring.data.redis.client-type` | jedis | Cliente Redis |
| `spring.mongodb.representation.uuid` | standard | Representação padrão de UUID no MongoDB |
| `**.jpa.properties.hibernate.hbm2ddl.auto` | update | Auto-criar/atualizar esquemas |

---

## 🧪 Testes

Execute os testes com:

```bash
./gradlew test
```

A suite de testes inclui:
- Testes unitários das entidades de domínio
- Testes de integração dos repositórios
- Testes dos controllers REST

---

## 📚 Estrutura de Arquivos de Suporte

```
marketplace/
├── build.gradle              # Configuração de dependências Gradle
├── compose.yml               # Definição dos serviços Docker
├── settings.gradle           # Configurações globais Gradle
├── gradle/                   # Wrapper do Gradle
└── build/                    # Artefatos compilados (ignorar no versionamento)
```

---

## 🛠️ Tecnologias e Dependências

- **Spring Boot**: 4.0.5
- **Java**: 25
- **Spring Data JPA**: ORM para MySQL e PostgreSQL
- **Spring Data MongoDB**: ODM para MongoDB
- **Spring Data Redis**: Client Redis
- **Spring Web**: REST APIs
- **Spring Actuator**: Monitoramento e Health Check
- **Spring Validation**: Validação de dados
- **Spring Data REST**: Auto-geração de APIs REST
- **Lombok**: Redução de boilerplate com anotações
- **MySQL Connector**: Driver MySQL
- **PostgreSQL Driver**: Driver PostgreSQL
- **Jedis**: Cliente Redis em Java

---

## 🌟 Principais Recursos

✅ **Arquitetura Modular**: Cada módulo é independente e testável  
✅ **Multi-Banco de Dados**: Adequado para cada caso de uso  
✅ **Cache Distribuído**: Redis integrado para performance  
✅ **Comunicação Assíncrona**: Event-driven entre módulos  
✅ **Controle de Concorrência**: Locks distribuídos para operações críticas  
✅ **Validação Automática**: Spring Validation em todos os endpoints  
✅ **Health Checks**: Monitoramento via Actuator  
✅ **Docker Compose**: Stack completo containerizado  

---

## 📞 Suporte e Contribuições

Este é um projeto educacional para aprender:
- Spring Boot e sua arquitetura
- Domain-Driven Design (DDD)
- Arquiteturas modulares distribuídas
- Integração com múltiplos bancos de dados

---

**Versão**: 0.0.1-SNAPSHOT  
**Autor**: Marcio  
**Data de Atualização**: 2026-01-20
