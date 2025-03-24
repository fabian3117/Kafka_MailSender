## Descripcion de proyecto
Este proyecto es una prueba de concepto que integra **Spring Boot** con **Kafka** para un servicio de facturación. En caso de fallo (simulado con una excepción), la cola de Kafka gestiona la contingencia.
El microservicio envía datos por Kafka para el envío de un correo electrónico. Aunque el envío lo maneja el microservicio, los datos necesarios están definidos en el archivo `application.properties`.
En la siguente revision de dara utilizad a la integracion con gRPC
### Tecnologias
- Docker Compose
- Kafka
- gRPC
- UML
- Mapper
- Java
- MySQL
- JPA
- Lombook
## Build
Para correr el proyecto primero se requiere correr el docker-compose a fin de tener las dependencias necesarias como
- MySQL
- Kafka
- Zookeeper.

Para lo cual es requisito tener en la pc el servicio de 
 > `docker-compose`.
 
En caso de no disponer del mismo verificar la documentacion oficial para su instalacion.
Comandos para correr atraves de docker-compose.
```bash
docker-compose up -d
```
Una ves realizado los procedimientos para docker-compose se puede proceder a correr el proyecto con springboot o atraves de la CLI.
Se deja a criterio del usuario la eleccion.
### Proyecto
```mermaid
sequenceDiagram
    participant Cliente
    participant Controller as FacturacionController
    participant Service as FacturaService
    participant Repository as FacturaRepository
    participant DB as Database
    participant Kafka as KafkaQueue

    %% Cliente realiza una solicitud GET
    Cliente->>FacturacionController: GET /facturacion/{userId}
    FacturacionController->>FacturaService: getFacturaById(userId)
    FacturaService->>FacturaRepository: findById(userId)
    FacturaRepository->>DB: SELECT * FROM facturas WHERE id = userId
    DB-->>FacturaRepository: FacturaDTO
    FacturaRepository-->>FacturaService: FacturaDTO
    FacturaService-->>FacturacionController: FacturaDTO
    FacturacionController-->>Cliente: FacturaDTO

    %% Cliente realiza una solicitud POST
    Cliente->>FacturacionController: POST /facturacion/{userId}
    FacturacionController->>FacturaService: saveNewFactura(facturaDTO)
    FacturaService->>FacturaRepository: save(facturaDTO)
    FacturaRepository->>DB: INSERT INTO facturas (id, fecha, monto) VALUES (facturaDTO)
    DB-->>FacturaRepository: Confirmation
    FacturaRepository-->>FacturaService: Confirmation
    FacturaService-->>FacturacionController: Confirmation
    FacturacionController-->>Cliente: 200 OK

    %% Cliente realiza una solicitud PUT
    Cliente->>FacturacionController: PUT /facturacion/{userId}
    FacturacionController->>FacturaService: updateFactura(userId, facturaDTO)
    FacturaService->>FacturaRepository: update(facturaDTO)
    FacturaRepository->>DB: UPDATE facturas SET ... WHERE id = userId
    DB-->>FacturaRepository: Confirmation
    FacturaRepository-->>FacturaService: Confirmation
    FacturaService-->>FacturacionController: Confirmation
    FacturacionController-->>Cliente: 200 OK

    %% Cliente realiza una solicitud DELETE
    Cliente->>FacturacionController: DELETE /facturacion/{userId}
    FacturacionController->>FacturaService: deleteFactura(userId)
    FacturaService->>FacturaRepository: deleteById(userId)
    FacturaRepository->>DB: DELETE FROM facturas WHERE id = userId
    DB-->>FacturaRepository: Confirmation
    FacturaRepository-->>FacturaService: Confirmation
    FacturaService-->>FacturacionController: Confirmation
    FacturacionController-->>Cliente: 200 OK

    %% Error al crear una factura, se envía mensaje a Kafka
    alt Error: UserId == 5
        FacturacionController-->>Cliente: RuntimeException("Error al crear la factura")
        FacturacionController->>Kafka: Send error message to Kafka
    end
```
##### Descripcion de microservicios

```mermaid
graph TD
  Zookeeper --> Kafka
  Kafka --> MySQL

  Zookeeper -->|Port 2181| Kafka
  Kafka -->|Port 9092| MySQL
  MySQL -->|Port 3306| Kafka
  subgraph ZookeeperConfig
    ZookeeperConfig1["ZOOKEEPER_CLIENT_PORT=2181"]
    ZookeeperConfig2["ZOOKEEPER_TICK_TIME=2000"]
  end
  subgraph KafkaConfig
    KafkaConfig1["KAFKA_BROKER_ID=1"]
    KafkaConfig2["KAFKA_ZOOKEEPER_CONNECT=zookeeper:2181"]
    KafkaConfig3["KAFKA_ADVERTISED_LISTENERS=PLAINTEXT://kafka:9092,PLAINTEXT_HOST://localhost:29092"]
    KafkaConfig4["KAFKA_LISTENER_SECURITY_PROTOCOL_MAP=PLAINTEXT:PLAINTEXT,PLAINTEXT_HOST:PLAINTEXT"]
    KafkaConfig5["KAFKA_INTER_BROKER_LISTENER_NAME=PLAINTEXT"]
    KafkaConfig6["KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR=1"]
  end
  subgraph MySQLConfig
    MySQLConfig1["MYSQL_ROOT_PASSWORD=clave"]
    MySQLConfig2["MYSQL_DATABASE=prueba"]
  end
```
### Descripcion del servicio ApiRest
```mermaid
sequenceDiagram
    participant User
    participant Controller as FacturacionController
    participant Service as FacturaService
    participant Kafka as KafkaQueue

    User->>FacturacionController: POST /facturacion/{userId}
    FacturacionController->>FacturaService: saveNewFactura(facturaDTO)
    alt UserId == 5
        FacturacionController-->>User: RuntimeException("Emulated error")
        FacturacionController->>Kafka: send Kafka message
    else UserId != 5
        FacturacionController-->>User: 200 OK
    end

    User->>FacturacionController: GET /facturacion/{userId}
    FacturacionController->>FacturaService: getFacturaById(userId)
    FacturacionController-->>User: FacturaDTO

    User->>FacturacionController: GET /facturacion/
    FacturacionController->>FacturaService: getAllFacturas()
    FacturacionController-->>User: List<FacturaModel>
```
Se genera una excepcion para poder iniciar el mensaje atraves del sistema de mensajeria de kafka.

### Modelado de datos
##### Email Clase
```mermaid
classDiagram
    class BodyMail {
        +String destino
        +String texto
        +String asunto
        +String tipo
        +BodyMail(String destino, String texto, String asunto, String tipo)
    }
```
#### Factura Entidad
En ambos casos se utiliza el modelo buildler para la construccion de los objetos
```mermaid
classDiagram
    class FacturaModel {
        +Long id
        +Date fecha
        +Long facturaId
        +Long monto
        +FacturaModel(Long id, Date fecha, Long facturaId, Long monto)
    }
```
### Arbol de arquitectura de proyecto
Detalle de las capas del proyecto

```
├── controller
│   └── FacturacionController.java
├── Demo2Application.java
├── dto
│   ├── BodyMail.java
│   └── FacturaDTO.java
├── exceptions
│   └── HandleExceptionCustomicer.java
├── grpc
│   └── grpcIntento.java
├── interceptorsJPA
│   └── FacturaInterceptorJPA.java
├── kafka
│   ├── EventServiceImpl.java
│   ├── producter
│   │   ├── KafkaProducterSender.java
│   │   └── topic
│   │       └── KafkaProducterTopic.java
│   ├── PruebaKafkaConsumer.java
│   └── setting
│       └── KafkaProducterSetting.java
├── kafkaLister.java
├── mappers
│   └── FacturacionMapper.java
├── models
│   └── FacturaModel.java
├── repository
│   └── facturaRepository.java
├── service
│   └── FacturaService.java
└── utils
    ├── MailDefault.java
    └── Utils.java
```

