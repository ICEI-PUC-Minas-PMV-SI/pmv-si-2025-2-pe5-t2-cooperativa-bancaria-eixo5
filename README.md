# Projeto de Infraestrutura de Rede para Cooperativa Bancária

### Curso: Sistemas de Informação
### Disciplina: Projeto – Projeto de Infraestrutura
### Eixo: 5

## 📘 Descrição do Projeto
Este projeto tem como objetivo planejar e implementar a infraestrutura de rede para uma cooperativa bancária em expansão no estado de Minas Gerais.
A matriz será sediada em Belo Horizonte, com cinco filiais nas cidades de:

#### Sete Lagoas
#### Divinópolis
#### Contagem
#### Nova Lima
#### Betim

## 👥 Integrantes
#### Luís Fernando Moura Santos
#### Cássio Venuto Monteiro
#### Júlia Persson Mascari
#### Paola Marques Braga
#### Pedro Augusto Teixeira Silva
#### Vinicius Henrique de Oliveira Neves

### Orientador: Alexandre Teixeira

### 🔐 Rotas da API (Backend Seguro)
#### 1. GET /health
✔️ Sucesso (200)
```
  {
    OK
  }
```

#### 2. POST /clients
```
 📤 Corpo da Requisição
 {
   "name": "João Silva",
   "email": "joao.silva@email.com",
   "password": "SenhaSegura123",
   "agency": "001",
   "account": 1234
 }
```

✔️ Sucesso (201)
```
{
    "id": "596862c8-c115-4bdc-88b3-ed708201ad78",
    "name": "João Silva",
    "email": "joao.silva@email.com",
    "account": 1234,
    "agency": "001"
}
```

❌ Erro (400)
```
{
    "error": "Dados inválidos",
    "message": "Email já cadastrado"
}
```

#### 3. POST /auth/login
```
📤 Corpo da Requisição
{
   "email": "joao.silva@email.com",
   "password": "SenhaSegura123"
}
```

✔️ Sucesso (200)
```
{
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "userId": "596862c8-c115-4bdc-88b3-ed708201ad78",
    "name": "João Silva"
}
```

❌ Erro (401)
```
{
"error": "Não autorizado"
}
```

#### 4. GET /clients/me
```
🔐 Cabeçalho
   Authorization: Bearer <token>
```

✔️ Sucesso (200)
```
{
    "id": "596862c8-c115-4bdc-88b3-ed708201ad78",
    "name": "João Silva",
    "email": "joao.silva@email.com",
    "account": 1234,
    "agency": "001"
}
```

❌ Erro (401)
```
{
    "error": "Não autorizado"
}
```

#### 5. PUT /clients/me
```
🔐 Cabeçalho
   Authorization: Bearer <token>
```

📤 Corpo da Requisição
```
{
    "name": "João Silva Atualizado",
    "email": "joao.novo@email.com"
}
```

✔️ Sucesso (200)
```
{
"id": "596862c8-c115-4bdc-88b3-ed708201ad78",
"name": "João Silva Atualizado",
"email": "joao.novo@email.com",
"account": 1234,
"agency": "001"
}
```