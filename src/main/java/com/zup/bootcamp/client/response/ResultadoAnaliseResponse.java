package com.zup.bootcamp.client.response;

public class ResultadoAnaliseResponse {

    private String documento;
    private String nome;
    private ResultadoSolicitacao resultadoSolicitacao;
    private String idProposta;

    @Deprecated
    public ResultadoAnaliseResponse() {
    }

    public ResultadoAnaliseResponse(String documento, String nome,
                                    ResultadoSolicitacao resultadoSolicitacao, String idProposta) {
        this.documento = documento;
        this.nome = nome;
        this.resultadoSolicitacao = resultadoSolicitacao;
        this.idProposta = idProposta;
    }

    public String getDocumento() {
        return documento;
    }

    public String getNome() {
        return nome;
    }

    public ResultadoSolicitacao getResultadoSolicitacao() {
        return resultadoSolicitacao;
    }

    public String getIdProposta() {
        return idProposta;
    }

    public boolean semRestricao() {
        return ResultadoSolicitacao.SEM_RESTRICAO.equals(resultadoSolicitacao);
    }

    public boolean temRestricao() {
        return ResultadoSolicitacao.COM_RESTRICAO.equals(resultadoSolicitacao);
    }

}
