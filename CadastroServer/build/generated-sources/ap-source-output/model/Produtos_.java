package model;

import java.math.BigDecimal;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import model.Movimentacao;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2023-09-12T22:21:28")
@StaticMetamodel(Produtos.class)
public class Produtos_ { 

    public static volatile SingularAttribute<Produtos, Integer> idProduto;
    public static volatile SingularAttribute<Produtos, BigDecimal> precoDeVenda;
    public static volatile SingularAttribute<Produtos, String> nome;
    public static volatile SingularAttribute<Produtos, Integer> quantidade;
    public static volatile CollectionAttribute<Produtos, Movimentacao> movimentacaoCollection;

}