
select 
pv_cliente.id_cliente,
pv_cliente.nm_cliente,
pv_proposta.id_proposta,
pv_proposta.ds_proposta,
pv_proposta.dt_vigencia,
pv_proposta.ds_status,
pv_proposta.vl_custo_pre_venda_real, 
pv_proposta.vl_receita_ajustada,
pv_proposta.vl_mcac,
pv_proposta.vl_receita_ajustada_conv,
pv_proposta.vl_mcac_conv,
pv_proposta.vl_custo_pre_venda_conv,
vl_proposta_conv,
pv_precificacao_operacao.id_precificacao,
pv_precificacao_operacao.in_spv,
pv_precificacao_operacao.ds_status,
pv_precificacao_operacao.vl_custo_pre_venda,
pv_precificacao_operacao.vl_receita_ajustada,
pv_precificacao_operacao.vl_mcac,
pv_projeto_operacao.id_projeto,
pv_projeto_operacao.ds_projeto,
pv_projeto_operacao.ds_status
from pv_proposta 
left join pv_cliente on (pv_cliente.id_cliente = pv_proposta.id_cliente)
left join pv_precificacao_operacao on (pv_proposta.id_proposta = pv_precificacao_operacao.id_proposta)
left join pv_proj_precif_operacao on (pv_precificacao_operacao.id_precificacao = pv_proj_precif_operacao.id_precificacao)
left join pv_projeto_operacao on (pv_proj_precif_operacao.id_projeto = pv_projeto_operacao.id_projeto)
where 
--pv_proposta.vl_proposta_conv is not null
vl_custo_pre_venda_conv <> 0
and (dt_vigencia > to_date('2016', 'yyyy') or dt_vigencia is null)
and pv_projeto_operacao.id_projeto is not null
order by pv_proposta.dt_vigencia desc nulls first
