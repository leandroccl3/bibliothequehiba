package biblio;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * Composant logiciel assurant la gestion des livres et des exemplaires
 * de livre.
 */
public class ComposantBDLivre {

  /**
   * Récupération de la liste complète des livres.
   * 
   * @return un <code>ArrayList<String[]></code>. Chaque tableau de chaînes
   * de caractères contenu correspond à un livre.<br/>
   * Il doit contenir 5 éléments (dans cet ordre) :
   * <ul>
   *   <li>0 : id</li>
   *   <li>1 : isbn10</li>
   *   <li>2 : isbn13</li>
   *   <li>3 : titre</li>
   *   <li>4 : auteur</li>
   * </ul>
   * @throws SQLException en cas d'erreur de connexion à la base.
   */
  public static ArrayList<String[]> listeTousLesLivres() throws SQLException {

    ArrayList<String[]> livres = new ArrayList<String[]>();

    Statement stmt = Connexion.getConnection().createStatement();
    String sql = "select * from livre";
    ResultSet rset = stmt.executeQuery(sql);

    while (rset.next()) {
      String[] livre = new String[5];
      livre[0] = rset.getString("id");
      livre[1] = rset.getString("isbn10");
      livre[2] = rset.getString("isbn13");
      livre[3] = rset.getString("titre");
      livre[4] = rset.getString("auteur");

      livres.add(livre);
    }
    rset.close();
    stmt.close();

    return livres;
  }

  /**
   * Retourne le nombre de livres référencés dans la base.
   * 
   * @return le nombre de livres.
   * @throws SQLException en cas d'erreur de connexion à la base.
   */
  public static int nbLivres() throws SQLException {
	  Statement stmt = Connexion.getConnection().createStatement();
	  String sql = "select count (id) as nbLivre from livre";
	  ResultSet rset = stmt.executeQuery(sql);
	  
	  int length= rset.getInt("nbLivre");
	  
	  rset.close();
	  stmt.close();
	    
	  return length;
  }

  /**
   * Récupération des informations sur un livre connu à partir de son identifiant.
   * 
   * @param idLivre : id du livre à rechercher
   * @return un tableau de chaînes de caractères (<code>String[]</code>). Chaque
   * tableau doit contenir 5 éléments (dans cet ordre) :
   * <ul>
   *   <li>0 : id</li>
   *   <li>1 : isbn10</li>
   *   <li>2 : isbn13</li>
   *   <li>3 : titre</li>
   *   <li>4 : auteur</li>
   * </ul>
   * @throws SQLException en cas d'erreur de connexion à la base.
   */
   public static String[] getLivre(int idLivre) throws SQLException {
	   String[] livre = new String[5];
	     
	     Statement stmt = Connexion.getConnection().createStatement();
		 String sql = "select * from livre where id ="+ idLivre+";";
		 ResultSet rset = stmt.executeQuery(sql);
		 
		  livre[0] = rset.getString("id");
	      livre[1] = rset.getString("isbn10");
	      livre[2] = rset.getString("isbn13");
	      livre[3] = rset.getString("titre");
	      livre[4] = rset.getString("auteur");
 
		  
		 rset.close();
		 stmt.close();
		    
		 return livre;
	     
   }
  
 /**
  * Récupération des informations sur un livre connu à partir de l'identifiant
  * de l'un de ses exemplaires.
  * 
  * @param idExemplaire : id de l'exemplaire
  * @return un tableau de chaînes de caractères (<code>String[]</code>). Chaque
  * tableau doit contenir 6 éléments (dans cet ordre) :
  * <ul>
  *   <li>0 : id de l'exemplaire</li>
  *   <li>1 : id du livre</li>
  *   <li>2 : isbn10</li>
  *   <li>3 : isbn13</li>
  *   <li>4 : titre</li>
  *   <li>5 : auteur</li>
  * </ul>
  * @throws SQLException en cas d'erreur de connexion à la base.
  */
  public static String[] getLivreParIdExemplaire(int idExemplaire) throws SQLException {
	  String[] livre = new String[6];
	    
	    
	    Statement stmt = Connexion.getConnection().createStatement();
		String sql = "select * from livre INNER JOIN examplaire on livre.id=examplaire.ID_examplaire where examplaire.ID_examplaire ="+ idExemplaire+";";
		ResultSet rset = stmt.executeQuery(sql);
		
		
		livre[0] = Integer.toString(idExemplaire);
		livre[1] = rset.getString("id");
	    livre[2] = rset.getString("isbn10");
	    livre[3] = rset.getString("isbn13");
	    livre[4] = rset.getString("titre");
	    livre[5] = rset.getString("auteur");
		
		rset.close();
		stmt.close();
		
		return livre;
	    
  }

  /**
   * Référencement d'un nouveau livre dans la base de données.
   * 
   * @param isbn10
   * @param isbn13
   * @param titre
   * @param auteur
   * @return l'identifiant (id) du livre créé.
   * @throws SQLException en cas d'erreur de connexion à la base.
   */
  public static int insererNouveauLivre(String isbn10, String isbn13, String titre, String auteur) throws SQLException {
	   
	  Statement stmt = Connexion.getConnection().createStatement();
      String sql = "insert into livre values(nextval('livre_id_seq'),'" + isbn10 +"','" + isbn13 + "','" +titre+"','"+ auteur +"');";
	  stmt.executeUpdate(sql);
	  
	  String sql1 = "select * from livre where isbn10 ="+ isbn10+";";
	  ResultSet rset = stmt.executeQuery(sql1);
	  
	  stmt.close();
	  
	  
	  return Integer.parseInt(rset.getString("id"));
	  
			  
  }
  
/**
   * Modification des informations d'un livre donné connu à partir de son
   * identifiant : les nouvelles valeurs (isbn10, isbn13, etc.) écrasent les
   * anciennes.
   * 
   * @param idLivre : id du livre à modifier.
   * @param isbn10 : nouvelle valeur d'isbn10.
   * @param isbn13 : nouvelle valeur d'isbn13.
   * @param titre : nouvelle valeur du titre.
   * @param auteur : nouvel auteur.
   * @throws SQLException en cas d'erreur de connexion à la base.
   */
  public static void modifierLivre(int idLivre, String isbn10, String isbn13, String titre, String auteur) throws SQLException {
	  
	  Statement stmt = Connexion.getConnection().createStatement();
      String sql = "UPDATE livre Set isbn10='"+isbn10+"', isnb13='"+isbn13+"',titre='"+titre+"',auteur='"+auteur+"'Where id="+idLivre+";";
	  stmt.executeUpdate(sql);
	  
	  stmt.close();
  }

  /**
   * Suppression d'un abonné connu à partir de son identifiant.
   * 
   * @param idLivre : id du livre à supprimer.
   * @throws SQLException en cas d'erreur de connexion à la base.
   */
   public static void supprimerLivre(int idLivre) throws SQLException {
	   
	    
	   Statement stmt = Connexion.getConnection().createStatement();
	   String sql = "Delete from livre where id ="+ idLivre +";" ;
	   stmt.executeUpdate(sql);
	   
	   
	   
	   stmt.close();  
   }

   /**
    * Retourne le nombre d'exemplaire d'un livre donné connu à partir
    * de son identifiant.
    * 
    * @param idLivre : id du livre dont on veut connaître le nombre d'exemplaires.
    * @return le nombre d'exemplaires
    * @throws SQLException en cas d'erreur de connexion à la base.
    */
   public static int nbExemplaires(int idLivre) throws SQLException {
	   Statement stmt = Connexion.getConnection().createStatement();
	   String sql = "select count (ID) as nbEX from examplaire where ID_livre="+idLivre+";";
	   ResultSet rset = stmt.executeQuery(sql);
		  
		  
	   int length = rset.getInt("nbEX");
	   rset.close();
	   stmt.close();
		    
		  
     return length;
   }

  /**
   * Récupération de la liste des identifiants d'exemplaires d'un livre donné
   * connu à partir de son identifiant.
   * 
   * @param idLivre : identifiant du livre dont on veut la liste des exemplaires.
   * @return un <code>ArrayList<Integer></code> contenant les identifiants des exemplaires
   * @throws SQLException en cas d'erreur de connexion à la base.
   */
  public static ArrayList<Integer> listeExemplaires(int idLivre) throws SQLException {
    ArrayList<Integer> exemplaires = new ArrayList<Integer>();
    
    Statement stmt = Connexion.getConnection().createStatement();
	String sql = "select ID from examplaire where ID_livre="+ idLivre +";";
	ResultSet rset = stmt.executeQuery(sql);
	
	int i;
	while (rset.next()) {
		  i=rset.getInt("ID");
		  
	      exemplaires.add(i);
	      
	}
    
	rset.close();
	stmt.close();
    
    return exemplaires;
  }

  /**
   * Ajout d'un exemplaire à un livre donné connu par son identifiant.
   * 
   * @param id identifiant du livre dont on veut ajouter un exemplaire.
   * @throws SQLException en cas d'erreur de connexion à la base.
   */
   public static void ajouterExemplaire(int idLivre) throws SQLException {
	   Statement stmt = Connexion.getConnection().createStatement();
	   String sql = "insert into examplaire values(nextval('examplaire_ID_seq'),'" + idLivre +"');";
	   stmt.executeUpdate(sql);
	   
	  
	   stmt.close();
   }

    /**
     * Suppression d'un exemplaire donné connu par son identifiant.
     * 
     * @param idExemplaire : identifiant du livre dont on veut supprimer un exemplaire.
     * @throws SQLException en cas d'erreur de connexion à la base.
     */
   public static void supprimerExemplaire(int idExemplaire) throws SQLException {
	   Statement stmt = Connexion.getConnection().createStatement();
	   String sql = "Delete from examplaire where ID ="+ idExemplaire +";" ;
	   stmt.executeUpdate(sql);
	   
	   
	   stmt.close();
   }

}
