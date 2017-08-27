select 
result.ok,
result.vl_proposta_conv, 
result.vl_proposta_conv_,
result.id_proposta,
pv_precificacao_operacao.id_precificacao,
pv_projeto_operacao.id_projeto,
pv_projeto_operacao.ds_projeto,
pv_cliente.id_cliente
from (
with sample as (
select pv_precificacao_operacao.id_proposta, count(*) vl_proposta_conv_
from
pv_projeto_operacao 
join pv_proj_precif_operacao
on (pv_projeto_operacao.id_projeto = pv_proj_precif_operacao.id_projeto)
join pv_precificacao_operacao 
on (pv_proj_precif_operacao.id_precificacao = pv_precificacao_operacao.id_precificacao)
group by pv_precificacao_operacao.id_proposta
)
select distinct 
decode(nvl(vl_proposta_conv, 0), nvl(vl_proposta_conv_, 0), 'OK', 'NOK') ok,
vl_proposta_conv, vl_proposta_conv_,
pv_proposta.id_proposta
from pv_proposta 
join sample 
on (pv_proposta.id_proposta = sample.id_proposta)
) result
left join pv_precificacao_operacao 
on (result.id_proposta = pv_precificacao_operacao.id_proposta)
left join pv_proj_precif_operacao
on (pv_precificacao_operacao.id_precificacao = pv_proj_precif_operacao.id_precificacao)
left join pv_projeto_operacao
on (pv_proj_precif_operacao.id_projeto = pv_projeto_operacao.id_projeto)
left join pv_cliente
on (pv_projeto_operacao.id_cliente = pv_cliente.id_cliente)
order by result.ok,  
pv_cliente.id_cliente,
pv_projeto_operacao.id_projeto,
result.id_proposta,
pv_precificacao_operacao.id_precificacao,
vl_proposta_conv