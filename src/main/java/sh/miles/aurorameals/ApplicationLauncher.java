package sh.miles.aurorameals;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sh.miles.aurorameals.data.db.RecipeDAO;
import sh.miles.aurorameals.data.db.RecipesCache;
import sh.miles.aurorameals.data.db.RecipesDatabase;
import sh.miles.aurorameals.system.OperatingSystem;
import sh.miles.raven.api.RavenAPI;
import sh.miles.raven.api.database.DatabaseConnection;
import sh.miles.raven.api.database.exception.DatabaseConnectionException;
import sh.miles.raven.api.support.DatabaseType;
import sh.miles.raven.core.RavenImplementation;

import java.util.List;

public class Main extends Application {

    public static final String CONNECTION_STRING = "nitrite:aurora-user@notsecured:%s";
    public static final String DATA_FOLDER = "aurora-planner";

    public static final Logger LOGGER = LogManager.getLogger("AuroraPlanner");

    @Override
    public void start(Stage stage) throws Exception {
        final Parent parent = Utils.loadFxmlFile("scenes/mainmenu.fxml");
        final Scene scene = new Scene(parent);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        if (OperatingSystem.getCurrent() == OperatingSystem.UNKNOWN) {
            // TODO: show error
            LOGGER.fatal("Unknown operating system");
            System.exit(MagicNumbers.EXIT_CODE_ERROR);
        }

        RavenAPI.setProvider(new RavenImplementation());
        final DatabaseConnection connection = RavenAPI.getProvider().getDatabaseConnection(DatabaseType.NITRITE);
        try {
            connection.connect(CONNECTION_STRING.formatted(OperatingSystem.getCurrent().getAppData(DATA_FOLDER)));
        } catch (DatabaseConnectionException e) {
            LOGGER.throwing(e);
            System.exit(MagicNumbers.EXIT_CODE_ERROR);
        }

        RecipesDatabase.getInstance().open(connection);
        RecipesDatabase.getInstance().getAllRecipes().whenComplete((List<RecipeDAO> recipes, Throwable error) -> {
            if (error != null) {
                LOGGER.throwing(error);
            }
            for (RecipeDAO recipe : recipes) {
                RecipesCache.getInstance().addRecipe(RecipeDAO.toRecipe(recipe));
            }
        });

        launch();
    }

}