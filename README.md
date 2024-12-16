ReadMe - VEXAPP - Aplicação Mobile de Carregamento para Carros Elétricos

Descrição

Esta aplicação foi desenvolvida para utilizadores de carros elétricos que buscam otimizar a busca e navegação até os pontos de carregamento em Lisboa. Através da integração com o Google Maps e Firebase, o app permite aos utilizadores autenticar-se, encontrar carregadores próximos e visualizar informações detalhadas sobre cada estação, incluindo o tipo, a potência e o endereço de cada ponto de carregamento. Além disso, a aplicação traça rotas até o carregador selecionado.

Funcionalidades

Autenticação de Utilizador: Utiliza o Firebase Authentication para permitir a criação de novos usuários e autenticação de usuários existentes. O utilizador pode se registrar, fazer login e acessar a plataforma.
Localização: A aplicação possui acesso à localização do utilizador, permitindo calcular a distância até os pontos de carregamento mais próximos.
Mapa de Carregadores: Através da integração com a API do Google Maps, é possível visualizar em um mapa os pontos de carregamento disponíveis em Lisboa.
Detalhes do Carregador: Cada ponto de carregamento é marcado no mapa com o tipo de carregador, potência, nível de carregamento e a morada física. O tipo de carregador pode variar entre diferentes padrões, como Tipo 1, Tipo 2, CHAdeMO, ou CCS, dependendo da estação.
Classificação de Potência: Cada ponto de carregamento possui uma classificação visual que indica a potência do carregador. A potência é representada por um número de raios, sendo:
1 Raio = Nível 1 (Potência baixa)
2 Raios = Nível 2 (Potência média)
3 Raios = Nível 3 (Alta potência)
4 Raios = Nível 4 (Potência muito alta)
Rota para o Carregador: Ao selecionar um ponto de carregamento, a aplicação traça a melhor rota até o carregador utilizando a API do Google Maps para indicar a direção até o destino.
Tecnologias Utilizadas

Kotlin: Linguagem de programação para desenvolvimento Android.
Firebase Authentication: Para gerenciamento de usuários e autenticação (login e registro).
Google Maps API: Para exibir o mapa, mostrar os pontos de carregamento e traçar rotas.
Firebase Firestore: Para armazenamento e gerenciamento dos dados de carregadores e preferências dos usuários.
