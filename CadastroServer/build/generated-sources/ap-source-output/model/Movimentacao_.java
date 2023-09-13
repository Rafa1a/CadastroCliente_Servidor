package model;

import java.math.BigDecimal;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import model.Pessoa;
import model.Produtos;
import model.Usuario;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2023-09-12T22:21:28")
@StaticMetamodel(Movimentacao.class)
public class Movimentacao_ { 

    public static volatile SingularAttribute<Movimentacao, BigDecimal> precoUnitario;
    public static volatile SingularAttribute<Movimentacao, String> tipo;
    public static volatile SingularAttribute<Movimentacao, Pessoa> pessoa;
    public static volatile SingularAttribute<Movimentacao, Produtos> produtos;
    public static volatile SingularAttribute<Movimentacao, Integer> idMovimentacao;
    public static volatile SingularAttribute<Movimentacao, Usuario> usuario;
    public static volatile SingularAttribute<Movimentacao, Integer> quantidadeES;

}