# language: pt
Funcionalidade: Aprovação de reserva

  Cenário: Responsável aprova uma reserva solicitada
    Dado que existe uma reserva aguardando aprovação
    Quando o responsável aprova a reserva
    Então o estado da reserva deve ser APROVADA
