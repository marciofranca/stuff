	select 
	vl_proposta_conv,
	count(*)
	from pv_proposta
	group by vl_proposta_conv
	order by vl_proposta_conv

	
select * from pv_proposta order by vl_proposta_conv desc

select * from all_tables where table_name like 'PV_%'

pv_proj_precif_operacao
select * from pv_precificacao_pre_venda

select pv_projeto_pre_venda.id_projeto, pv_proj_precif_pre_venda.id_precificacao, pv_precificacao_pre_venda.id_proposta, pv_projeto_pre_venda.vl_custo_realizado 
from
pv_projeto_pre_venda full join 
pv_proj_precif_pre_venda
on (pv_projeto_pre_venda.id_projeto = pv_proj_precif_pre_venda.id_projeto) full join
pv_precificacao_pre_venda 
on (pv_proj_precif_pre_venda.id_precificacao = pv_precificacao_pre_venda.id_precificacao)
order by pv_projeto_pre_venda.vl_custo_realizado nulls first


select 
result.ok,
result.ok2,
result.vl_custo_pre_venda_real, 
result.vl_custo_pre_venda_real_,
result.id_proposta,
pv_precificacao_pre_venda.id_precificacao,
pv_projeto_pre_venda.id_projeto,
pv_projeto_pre_venda.ds_projeto,
pv_cliente.id_cliente
from (
with sample as (
select pv_precificacao_pre_venda.id_proposta, sum(pv_projeto_pre_venda.vl_custo_realizado) vl_custo_pre_venda_real_
from
pv_projeto_pre_venda 
join pv_proj_precif_pre_venda
on (pv_projeto_pre_venda.id_projeto = pv_proj_precif_pre_venda.id_projeto)
join pv_precificacao_pre_venda 
on (pv_proj_precif_pre_venda.id_precificacao = pv_precificacao_pre_venda.id_precificacao)
group by pv_precificacao_pre_venda.id_proposta
)
select distinct 
decode(nvl(vl_custo_pre_venda_real, 0), nvl(vl_custo_pre_venda_real_, 0), 'OK', 'NOK') ok,
decode(nvl(vl_custo_pre_venda_real, 0), nvl(vl_custo_pre_venda_real_/2, 0), 'OK', 'NOK') ok2,
vl_custo_pre_venda_real, vl_custo_pre_venda_real_,
pv_proposta.id_proposta
from pv_proposta 
join sample 
on (pv_proposta.id_proposta = sample.id_proposta)
) result
left join pv_precificacao_pre_venda 
on (result.id_proposta = pv_precificacao_pre_venda.id_proposta)
left join pv_proj_precif_pre_venda
on (pv_precificacao_pre_venda.id_precificacao = pv_proj_precif_pre_venda.id_precificacao)
left join pv_projeto_pre_venda
on (pv_proj_precif_pre_venda.id_projeto = pv_projeto_pre_venda.id_projeto)
left join pv_cliente
on (pv_projeto_pre_venda.id_cliente = pv_cliente.id_cliente)
order by result.ok,  
pv_cliente.id_cliente,
pv_projeto_pre_venda.id_projeto,
result.id_proposta,
pv_precificacao_pre_venda.id_precificacao,
vl_custo_pre_venda_real


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

30883
32324
42090
33065



select 
result.ok,
result.ok2,
result.vl_custo_pre_venda_real, 
result.vl_custo_pre_venda_real_,
result.id_proposta,
pv_precificacao_pre_venda.id_precificacao,
pv_projeto_pre_venda.id_projeto,
pv_projeto_pre_venda.ds_projeto,
pv_cliente.id_cliente
from (
with sample as (
select pv_precificacao_pre_venda.id_proposta, sum(pv_projeto_pre_venda.vl_custo_realizado) vl_custo_pre_venda_real_
from
pv_projeto_pre_venda 
join pv_proj_precif_pre_venda
on (pv_projeto_pre_venda.id_projeto = pv_proj_precif_pre_venda.id_projeto)
join pv_precificacao_pre_venda 
on (pv_proj_precif_pre_venda.id_precificacao = pv_precificacao_pre_venda.id_precificacao)
group by pv_precificacao_pre_venda.id_proposta
)
select distinct 
decode(nvl(vl_custo_pre_venda_real, 0), nvl(vl_custo_pre_venda_real_, 0), 'OK', 'NOK') ok,
decode(nvl(vl_custo_pre_venda_real, 0), nvl(vl_custo_pre_venda_real_/2, 0), 'OK', 'NOK') ok2,
vl_custo_pre_venda_real, vl_custo_pre_venda_real_,
pv_proposta.id_proposta
from pv_proposta 
join sample 
on (pv_proposta.id_proposta = sample.id_proposta)
) result
left join pv_precificacao_pre_venda 
on (result.id_proposta = pv_precificacao_pre_venda.id_proposta)
left join pv_proj_precif_pre_venda
on (pv_precificacao_pre_venda.id_precificacao = pv_proj_precif_pre_venda.id_precificacao)
left join pv_projeto_pre_venda
on (pv_proj_precif_pre_venda.id_projeto = pv_projeto_pre_venda.id_projeto)
left join pv_cliente
on (pv_projeto_pre_venda.id_cliente = pv_cliente.id_cliente)
order by result.ok,  
pv_cliente.id_cliente,
pv_projeto_pre_venda.id_projeto,
result.id_proposta,
pv_precificacao_pre_venda.id_precificacao,
vl_custo_pre_venda_real
