Feature: Aprovacao de Reserva
  Scenario: Aprovacao por responsavel
    Dado que existe uma reserva solicitada
    Quando o responsavel tenta aprovar a reserva
    Entao o status da reserva deve ser APROVADA
