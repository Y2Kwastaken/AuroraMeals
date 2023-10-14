package sh.miles.aurorameals.data.db;

import com.spotify.futures.CompletableFutures;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import sh.miles.aurorameals.Main;
import sh.miles.raven.api.database.Database;
import sh.miles.raven.api.database.DatabaseConnection;
import sh.miles.raven.datamapper.v2.database.DataContainerCollection;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * Database that contains recipes
 */
public final class RecipesDatabase {

    public static final String DATABASE_NAME = "recipes";
    public static final String COLLECTION_NAME = "recipes";
    public static final String ENTRY_ID = "list";

    private static RecipesDatabase instance;
    private static Logger logger = LogManager.getLogger("RecipesDatabase");

    private DatabaseConnection connection;
    private DataContainerCollection collection;
    private Database database;

    private RecipesDatabase() {
    }

    public void open(@NotNull final DatabaseConnection connection) {
        this.connection = connection;
        this.database = connection.getDatabase(DATABASE_NAME);
        this.collection = new DataContainerCollection(database, COLLECTION_NAME);
    }

    public void close() {
        this.connection.disconnect();
        this.database = null;
        this.collection = null;
    }

    public CompletableFuture<RecipeDAO> getRecipe(@NotNull final UUID uuid) {
        return CompletableFuture.supplyAsync(() -> collection.getDataContainer(uuid.toString(), RecipeDAO.class));
    }

    public CompletableFuture<List<RecipeDAO>> getAllRecipes() {
        return CompletableFuture.supplyAsync(() -> database.getAllSections(COLLECTION_NAME)).thenApplyAsync((List<String> ids) -> {
            List<CompletableFuture<RecipeDAO>> recipes = new ArrayList<>();
            for (String id : ids) {
                Main.LOGGER.info("Loading Recipe with id of %s".formatted(id));
                recipes.add(CompletableFuture.supplyAsync(() -> collection.getDataContainer(id, RecipeDAO.class)));
            }
            return recipes;
        }).thenApplyAsync((List<CompletableFuture<RecipeDAO>> recipes) -> {
            final CompletableFuture<List<RecipeDAO>> recipeFixed = CompletableFutures.allAsList(recipes);
            try {
                return recipeFixed.get();
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        }).exceptionally((Throwable error) -> {
            Main.LOGGER.throwing(error);
            return new ArrayList<>();
        });
    }

    public void saveRecipe(@NotNull final RecipeDAO recipeDAO) {
        CompletableFuture.runAsync(() -> {
            Main.LOGGER.info("Saving Recipe %s with content %s".formatted(recipeDAO.getUniqueId(), recipeDAO.getName()));
            this.collection.setContainer(recipeDAO.getUniqueId().toString(), recipeDAO);
        });
    }

    public void deleteRecipe(@NotNull final RecipeDAO recipeDAO) {
        CompletableFuture.runAsync(() -> {
            this.collection.removeDataContainer(recipeDAO.getUniqueId().toString(), recipeDAO);
            Main.LOGGER.info("Deleted Recipe %s with content %s".formatted(recipeDAO.getUniqueId().toString(), recipeDAO));
        });
    }

    public void saveAll(@NotNull final List<RecipeDAO> recipeDAOs) {
        for (RecipeDAO recipeDAO : recipeDAOs) {
            saveRecipe(recipeDAO);
        }
    }

    public void saveAllSync(@NotNull final List<RecipeDAO> recipeDAOs) {
        for (RecipeDAO recipeDAO : recipeDAOs) {
            Main.LOGGER.info("Saving Recipe %s with name %s".formatted(recipeDAO.getUniqueId(), recipeDAO.getName()));
            this.collection.setContainer(recipeDAO.getUniqueId().toString(), recipeDAO);
        }
    }

    public static RecipesDatabase getInstance() {
        if (instance == null) {
            instance = new RecipesDatabase();
        }
        return instance;
    }
}
