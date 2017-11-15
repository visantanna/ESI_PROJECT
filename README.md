# ESI_PROJECT
Repositório de códigos e documentos sobre o trabalho de ESI

TUTORIAL GITHUB

primeiramente baixe o git no seu windows:

https://www.atlassian.com/git/tutorials/install-git#windows

após a instalação ser devidamente concluída você deve entrar no CMD ,acessar a pasta onde você deseja baixar o projeto e digitar os seguintes comandos:

git clone https://github.com/visatanna/ESI_PROJECT
git pull  https://github.com/visatanna/ESI_PROJECT

após esses dois comandos note que a pasta ESI_PROJECT foi devidamente baixada para a pasta indicada.

para subir alterações no código você deve usar os seguintes comandos (não se esqueça de entrar na pasta do projeto que você está atualizando):

git pull https://github.com/visatanna/ESI_PROJECT
git add <nome-do-arquivo-a-ser-atualizado> - (se você quiser subir a pasta toda escreva :"git add .")
//Obs: se você não sabe exatamente a classe que você adicionou/modificou não tem problema! ao escrever "git add ." o git adiciona todos os arquivos que foram alterados/adicionados.
git commit -m "mensagem de commit" - (entre as aspas coloque uma mensagem explicando o que foi alterado no commit!)
git push https://github.com/visatanna/ESI_PROJECT - (Só aqui que ele realmente sobe o código)
e-mail do git hub
senha do git hub

Após esses comandos seu código estará no projeto do github, disponivel a todos!

TUTORIAL MAVEN

Para importar um projeto com maven precisaremos dos seguintes passos:

No eclipse : File -> Import ->Maven->Existing Maven Projects (pressione next) -> selecione pom.xml (Pressione finish)

pronto você já tem o projeto , contudo você ainda não baixou as dependencias (libs que esse projeto depende para funcionar), portanto: 

Pressione Alt+f5 e clique em Force Update of Snapshots/Releases , pronto , todas as dependencias foram baixadas
//OBS: você precisa ter o java 1.8 instalado no seu computador para o projeto funcionar

Caso precise adicionar uma dependencia nova no projeto :

clique no arquivo pom.xml dentro da pasta principal do projeto e adicione a dependencia.

Exemplo: para adicionar a dependencia no maven javax.email nós achamos no google a lib correspondente no google:
https://mvnrepository.com/artifact/javax.mail/mail/1.5.0-b01:

<dependency>
    <groupId>javax.mail</groupId>
    <artifactId>mail</artifactId>
    <version>1.5.0-b01</version>
</dependency>

e adicionamos esse "endereço" dentro do pom na tag <dependencies> e atualizamos novamente as dependencias (com o alt+f5 de novo) 

assim todos terão acesso as dependencias adicionadas.

