# Catálogo de Filmes Favoritos

App Android desenvolvido para o desafio de Desenvolvimento Mobile: o usuário
visualiza e edita seus filmes favoritos (nome, nota e foto), com **MVVM**,
**View Binding** e **Coil**.

## Integrantes

- Guilherme Verrone - 216361
- Murilo Alvim - 240874
- Yasmin Batista - 241959

## Funcionalidades

- **Login**: credenciais fixas no código (`admin` / `1234`).
- **Lista de favoritos**: cards com a foto (carregada da URL pelo Coil), nome,
  gênero e nota exibida em estrelas. A lista já abre com três filmes de exemplo.
- **Cadastrar filme**: nome, gênero, nota (0 a 10) e URL do poster, com
  pré-visualização da imagem enquanto a URL é digitada.
- **Editar filme**: o botão *Editar* do card abre o mesmo formulário preenchido;
  ao salvar, o card é atualizado na lista.
- **Excluir filme**: com diálogo de confirmação e Snackbar de retorno.
- Validação de campos obrigatórios e da nota (aceita `8.5` ou `8,5`).

## Arquitetura (MVVM)

```
com.example.desafio_ddm
├── model/      Movie                      (M) dados do filme
├── data/       MovieRepository            fonte única de dados, publica LiveData
├── viewmodel/  MovieViewModel             (VM) regras de cadastro/edição/exclusão
│               LoginViewModel             (VM) validação do login
└── ui/         MainActivity, ListaActivity,
                CadastroActivity, MovieAdapter   (V) só exibem e repassam eventos
```

- As Activities **não** contêm regra de negócio: elas observam `LiveData` da
  ViewModel (`movies`, `isEmpty`) e chamam `save` / `delete`.
- A validação do formulário fica em `MovieViewModel.save`, que devolve um
  `SaveResult` para a View escolher a mensagem.
- O `MovieRepository` é a fonte única de dados; como ele publica a lista em um
  `LiveData`, editar ou excluir um filme atualiza a tela de lista
  automaticamente, sem `notifyDataSetChanged`.
- O `MovieAdapter` usa `ListAdapter` + `DiffUtil`, então as alterações são
  animadas item a item.
- Todo acesso a views é por **View Binding** (`ActivityListaBinding`,
  `ItemMovieBinding`, etc.) — não há `findViewById`.

> Os dados ficam em memória (o desafio não pede persistência), então são
> perdidos ao fechar o app.

## Tecnologias

- Kotlin, View Binding
- ViewModel + LiveData (`androidx.lifecycle`)
- RecyclerView (`ListAdapter` + `DiffUtil`) e Material Components
- [Coil](https://github.com/coil-kt/coil) para carregar as imagens via URL

## Como rodar

1. Abra o projeto no Android Studio (versão recente, AGP 9.x).
2. Aguarde a sincronização do Gradle.
3. Rode no emulador ou dispositivo físico (`minSdk 24`).
4. Faça login com usuário `admin` e senha `1234`.
5. Na lista, use *Editar* / *Excluir* nos cards ou *Adicionar filme* para
   cadastrar um novo (informe uma URL de imagem válida, ex.:
   `https://picsum.photos/seed/filme/300/450`).

## Testes

Testes unitários da lógica da ViewModel (validação, cadastro, edição e
exclusão):

```
./gradlew test
```

