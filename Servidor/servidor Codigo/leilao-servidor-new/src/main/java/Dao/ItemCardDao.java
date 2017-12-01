package Dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.leilao.DirectConnection;
import com.leilao.bancoDeDados.Instituicao;
import com.leilao.bancoDeDados.ItemCard;

public class ItemCardDao {
	
	
	public List<ItemCard> buscarCardItens( int offset , int qtd , String categoria){
		List<ItemCard> cardlist = new ArrayList<ItemCard>();
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Timestamp now = new Timestamp(System.currentTimeMillis());
		String dataAtual = sdf.format(now);
		
		String query = "SELECT item.cod_item ,item.nome as nome , item.categoria as categoria , item.descricao as descricao, item.lance_minimo as lance_minimo, item.foto as foto "
	     		+ ", lista.quantidade as qtd, lista.valor_atual as valor_atual, leilao.data_inicio as data_inicio"
	     		+ ", leilao.data_termino as data_termino , inst.nome as nomeInst , inst.cod_instituicao as codInst , inst.telefone as tel , inst.email as emailInst, "
	     		+ "  inst.estado as estado ,inst.cidade as cidade, inst.rua as rua, inst.numero as numero, inst.complemento as comp from Item "
	     		+ " inner join Lista_De_Itens as lista on (item.cod_item = lista.cod_item ) "
	     		+ " inner join Leilao on (leilao.cod_leilao = lista.cod_leilao)"
	     		+ " inner join Instituicao as inst on (leilao.cod_instituicao = inst.cod_instituicao)"
	     		+ " where '"+ dataAtual +"' between leilao.data_inicio and data_termino";
		if(!"NENHUM".equals(categoria)){
			query = query + " and leilao.categoria = " + categoria;
		}
		
	     try{
	    	 Connection con = DirectConnection.getConnection();
	    	 Statement st = con.createStatement();
	    	 ResultSet rs = st.executeQuery(query);
	    	 
	    	 while(rs.next()){
	    		 ItemCard item = new ItemCard();
	    		 Instituicao instituicao = new Instituicao();
	    		 instituicao.setCidade(rs.getString("cidade"));
	    		 instituicao.setComplemento(rs.getString("comp"));
	    		 instituicao.setCod_instituicao(rs.getInt("codInst"));
	    		 instituicao.setEmail(rs.getString("emailInst"));
	    		 instituicao.setNome(rs.getString("nomeInst"));
	    		 instituicao.setTelefone(rs.getString("tel"));
	    		 instituicao.setEstado(rs.getString("estado"));
	    		 
	    		 item.setInstituicao(instituicao);
	    		 item.setNome(rs.getString("nome"));
	    		 item.setCategoria(rs.getString("categoria"));
	    		 item.setDescricao(rs.getString("descricao"));
	    		 item.setLance_minimo(rs.getString("lance_minimo"));
	    		 item.setFoto(rs.getBytes("foto"));
	    		 item.setQuantidade(rs.getInt("qtd"));
	    		 item.setLance_atual(rs.getString("valor_atual"));
	    		 item.setDataInicio(rs.getString("data_inicio"));
	    		 item.setDataFim(rs.getString("data_termino"));
	    		 item.setDataInicio(rs.getString("data_inicio"));
	    		 item.setDataInicio(rs.getString("data_inicio"));
	    		 item.setInstituicao(instituicao);
	    		 cardlist.add(item);
	    	 }
			
	     }catch(Exception e){
	    	 e.printStackTrace();
	     }
	     return cardlist;
	}
}
