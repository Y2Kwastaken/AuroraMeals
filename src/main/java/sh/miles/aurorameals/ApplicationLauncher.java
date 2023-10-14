package sh.miles.aurorameals;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import sh.miles.aurorameals.data.cache.RecipeCache;
import sh.miles.aurorameals.data.db.RecipeDAO;
import sh.miles.aurorameals.data.db.RecipesDatabase;
import sh.miles.aurorameals.gui.GuiConstants;
import sh.miles.aurorameals.gui.StageController;

public final class ApplicationLauncher extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        StageController.setInstance(stage, (mainStage) -> {
            mainStage.setTitle("Aurora Meal Planner");
            mainStage.getIcons().add(GuiConstants.ICON);
        });
        StageController.getInstance().registerScene(GuiConstants.CREATOR_SCENE, "meal-creator", "app", "aurora-style");
        StageController.getInstance().registerScene(GuiConstants.HOME_SCENE, "meal-home", "app", "aurora-style");
        final Scene scene = StageController.getInstance().getScene(GuiConstants.HOME_SCENE);
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void stop() throws Exception {
        RecipesDatabase.getInstance().saveAllSync(RecipeCache.instance.toList().stream().map(RecipeDAO::fromRecipe).toList());
        RecipesDatabase.getInstance().close();
    }
}
