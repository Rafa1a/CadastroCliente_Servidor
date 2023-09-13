package model;

import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import model.Movimentacao;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2023-09-12T22:21:28")
@StaticMetamodel(Usuario.class)
public class Usuario_ { 

    public static volatile SingularAttribute<Usuario, String> senha;
    public static volatile SingularAttribute<Usuario, String> logon;
    public static volatile SingularAttribute<Usuario, Integer> idUsuario;
    public static volatile CollectionAttribute<Usuario, Movimentacao> movimentacaoCollection;

}