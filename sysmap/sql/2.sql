select id_precificacao, count(*) from pv_proj_precif_pre_venda group by id_precificacao having count(*) > 1 order by count(*), id_precificacao;

select * from pv_proposta
left join pv_precificacao_pre_venda 
on (pv_proposta.id_proposta = pv_precificacao_pre_venda.id_proposta)
left join pv_proj_precif_pre_venda
on (pv_precificacao_pre_venda.id_precificacao = pv_proj_precif_pre_venda.id_precificacao)
left join pv_projeto_pre_venda
on (pv_proj_precif_pre_venda.id_projeto = pv_projeto_pre_venda.id_projeto)
left join pv_cliente
on (pv_projeto_pre_venda.id_cliente = pv_cliente.id_cliente)
where pv_precificacao_pre_venda.id_precificacao in(38027)