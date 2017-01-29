package com.piria2016.ok;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import dao.DaoFactory;
import dao.DaoUtils;

@Path("/")
public class GenreService {

	
	private static final String SQL_GET_ALL_MOVIES_BY_GENRE = "SELECT m.* FROM movies m JOIN movies_has_genres mhg ON m.id = mhg.movies_id JOIN genres g ON g.id = mhg.genres_id WHERE g.active=1 AND g.name LIKE ?;";
	private static final String SQL_GET_ALL_ACTORS_BY_MOVIE_ID = "SELECT a.id, a.name FROM movies_has_actors ma JOIN actors a ON a.id = ma.actors_id WHERE ma.movies_id = ?;";
	private static final String SQL_GET_ALL_GENRES_BY_MOVIE_ID = "SELECT g.id, g.name FROM movies_has_genres mg JOIN genres g ON g.id = mg.genres_id WHERE mg.movies_id = ?;";

	
	@GET
	@Path("/movieByGenre/{genre}")
//	@Produces(MediaType.TEXT_PLAIN)
	@Produces(MediaType.APPLICATION_JSON)
	public String getMoviesByGenre(@PathParam("genre") String genre) {
		DaoFactory daoFactory = DaoFactory.getInstance();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		JSONArray jArray = new JSONArray();
		
		try {
			conn = daoFactory.checkOut();
			System.out.println("genre: " + genre);

			pstmt = DaoUtils.prepareStatement(conn, SQL_GET_ALL_MOVIES_BY_GENRE, false, new Object[] { "%" + genre + "%" });
			rs = pstmt.executeQuery();
			while(rs.next()){
				
				JSONObject movieJson = new JSONObject();
				Integer movieId = rs.getInt(1);
				System.out.println("movie id: " + movieId);
				movieJson.put("id", movieId);
				movieJson.put("title", rs.getString(2));
				movieJson.put("releaseDate", rs.getDate(3) + "");
				movieJson.put("storyLine", rs.getString(4));
				movieJson.put("trailerLocationType", rs.getInt(5) + "");
				movieJson.put("trailerLocation", rs.getString(6));
				movieJson.put("runtimeMinutes", rs.getInt(7) + "");
				movieJson.put("rate", rs.getDouble(8) + "");
				
				movieJson.put("actors", getActorsByMovieId(movieId));
				movieJson.put("genres", getGenresByMovieId(movieId));
				
				jArray.put(movieJson);

			}


			System.out.println("from rest genres: " + jArray.toString());

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if(rs != null)
					rs.close();
				if(pstmt != null)
					pstmt.close();
				if(conn != null)
					daoFactory.checkIn(conn);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		
		return jArray.toString();
//		System.out.println("jeeeeeeeeeeeeeeeeeeeee");
//		return "jeee";
	}
	
	private String getActorsByMovieId(int movieId){
		DaoFactory daoFactory = DaoFactory.getInstance();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		JSONArray jArray = new JSONArray();
		
		try {
			conn = daoFactory.checkOut();
			
			pstmt = DaoUtils.prepareStatement(conn, SQL_GET_ALL_ACTORS_BY_MOVIE_ID, false, new Object[]{movieId});
			rs = pstmt.executeQuery();
			while(rs.next()){
				JSONObject actorJson = new JSONObject();
				actorJson.put("id", rs.getInt("id"));
				actorJson.put("name", rs.getString("name"));
				jArray.put(actorJson);
			}

			System.out.println("from rest actors: " + jArray.toString());

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				rs.close();
				pstmt.close();
				daoFactory.checkIn(conn);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		
		return jArray.toString();
	}
	
	
	
	private String getGenresByMovieId(int movieId){
		DaoFactory daoFactory = DaoFactory.getInstance();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		JSONArray jArray = new JSONArray();
		
		try {
			conn = daoFactory.checkOut();
			pstmt = DaoUtils.prepareStatement(conn, SQL_GET_ALL_GENRES_BY_MOVIE_ID, false, new Object[] { movieId });
			rs = pstmt.executeQuery();
			while(rs.next()){
				JSONObject genreJson = new JSONObject();
				genreJson.put("id", rs.getInt("id"));
				genreJson.put("name", rs.getString("name"));
				jArray.put(genreJson);
			}

			System.out.println("from rest genres: " + jArray.toString());

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				rs.close();
				pstmt.close();
				daoFactory.checkIn(conn);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		
		return jArray.toString();
	}
	
	
}
