package com.libgdx.game.space.shooter.screens;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.libgdx.game.space.*;
import com.libgdx.game.space.shooter.screens.entities.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Locale;

public class GameScreen implements Screen {

    // Définition des constantes pour les dimensions du monde et la sensibilité tactile
    private final float WORLD_WIDTH = SpaceShooterGame.WINDOW_WIDTH;
    private final float WORLD_HEIGHT = SpaceShooterGame.WINDOW_HEIGHT;
    private final float TOUCH_THRESHOLD = 0.5f;
    private Music backgroundMusic;

    // Référence au jeu SpaceShooterGame et à la police BitmapFont
    SpaceShooterGame game;
    BitmapFont font;

    // Variables pour le positionnement de l'interface utilisateur
    float hudVerticalMargin, hudLeftX, hudRightX, hudCenterX, hudRow1Y, hudRow2Y, hudSectionWidth;

    // Caméra et viewport pour la gestion de l'affichage
    private final Camera camera;
    private final Viewport viewport;

    // SpriteBatch pour le rendu graphique
    private final SpriteBatch batch;

    // Textures utilisées dans le jeu
    private final Texture[] backgrounds;
    private final Texture playerTexture;
    private final Texture enemySmallTexture;
    private final Texture enemyMediumTexture;
    private final Texture ennemyBigTexture;
    private final Texture playerProjectileTexture;
    private final Texture enemyProjectileTexture;
    private final Texture healthPickUpTexture;
    private final Texture powerUpPickUpTexture;

    // Paramètres de défilement des fonds d'écran
    private final float[] backgroundOffsets = {0, 0, 0, 0};
    private final float bgMaxScrollingSpeed;

    // Timers pour le spawn des bonus de puissance et de santé
    private final float timeBetweenPowerUpSpawns = 15f;
    private float powerUpSpawnTimer = 0f;
    private final float timeBetweenHealthUpSpawns = 4f;
    private float healthUpSpawnTimer = 0f;

    // Objets du jeu
    private final Player player;
    private final LinkedList<Enemy> enemies;
    private final LinkedList<Projectile> playerProjectileList;
    private final LinkedList<Projectile> enemyProjectileList;
    private final LinkedList<BoostEntity> pickUps;

    // Score et numéro de vague
    private int score;
    private final int waveNumber;

    // Lecteur de fichiers
    private FileReader fileReader;
    private final BufferedReader reader;


    //constructeur qui initialise divers éléments nécessaires pour le fonctionnement du jeu
    public GameScreen(SpaceShooterGame game) {
        // Référence au jeu principal
        this.game = game;

        // Initialisation de la caméra et du viewport
        camera = new OrthographicCamera();
        viewport = new StretchViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);

        // Chargement des textures des fonds d'écran
        backgrounds = new Texture[4];
        backgrounds[0] = new Texture("Starscape00.png");
        backgrounds[1] = new Texture("Starscape01.png");
        backgrounds[2] = new Texture("Starscape02.png");
        backgrounds[3] = new Texture("Starscape03.png");

        // Calcul de la vitesse maximale de défilement des fonds d'écran
        bgMaxScrollingSpeed = (float) (WORLD_HEIGHT) / 4;

        // Chargement des textures des différents éléments du jeu
        playerTexture = new Texture("ship.png");
        enemySmallTexture = new Texture("enemy-small.png");
        enemyMediumTexture = new Texture("enemy-medium.png");
        ennemyBigTexture = new Texture("enemy-big.png");
        playerProjectileTexture = new Texture("laser-one.png");
        enemyProjectileTexture = new Texture("bolt-fill.png");
        healthPickUpTexture = new Texture("heart_icon.png");
        powerUpPickUpTexture = new Texture("power-up.png");

        // Création du joueur avec des paramètres prédéfinis
        player = new Player(400f, 7, WORLD_WIDTH / 2, WORLD_HEIGHT / 4, 22, 34, playerTexture, 10f, 5f, 500f, 0.3f, playerProjectileTexture);

        // Initialisation des listes pour les ennemis, les projectiles et les bonus
        enemies = new LinkedList<>();
        playerProjectileList = new LinkedList<>();
        enemyProjectileList = new LinkedList<>();
        pickUps = new LinkedList<>();

        // Numéro de la vague actuelle
        waveNumber = 1;

        // Création du SpriteBatch pour le rendu graphique
        batch = new SpriteBatch();

        // Préparation de l'interface utilisateur
        prepareHUD();

        // Chargement du fichier de données de la vague actuelle
        try {
            fileReader = new FileReader("waveData/wave_" + waveNumber + "_data.txt");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        reader = new BufferedReader(fileReader);

        // Lancement de la prochaine vague du jeu
        try {
            nextWave();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        backgroundMusic =Gdx.audio.newMusic(Gdx.files.internal("Musique.mp3"));
        backgroundMusic.setLooping(true);
        backgroundMusic.setVolume(0.5f);
    }


    //effectue les opérations nécessaires pour préparer l'affichage de l'interface utilisateur (HUD)
    //Le HUD est une interface graphique qui superpose des informations importantes sur l'écran de jeu
    private void prepareHUD() {
        // Création d'un générateur de polices de caractères à partir du fichier "Roboto-Regular.ttf"
        FreeTypeFontGenerator fontGenerator = new FreeTypeFontGenerator(
                Gdx.files.internal("Roboto-Regular.ttf"));

        // Configuration des paramètres de la police de caractères
        FreeTypeFontGenerator.FreeTypeFontParameter fontParameter =
                new FreeTypeFontGenerator.FreeTypeFontParameter();
        fontParameter.size = 35;  // Taille de la police
        fontParameter.borderWidth = 3.6f;  // Largeur de la bordure
        fontParameter.color = new Color(1, 1, 1, 0.3f);  // Couleur du texte (blanc avec une transparence de 0.3)
        fontParameter.borderColor = new Color(0, 0, 0, 0.3f);  // Couleur de la bordure (noir avec une transparence de 0.3)

        // Génération de la police de caractères à partir des paramètres spécifiés
        font = fontGenerator.generateFont(fontParameter);

        // Configuration de l'échelle de la police
        font.getData().setScale(0.4f);

        // Calcul des marges verticales du HUD en fonction de la hauteur de la police
        hudVerticalMargin = font.getCapHeight() / 2;

        // Calcul des positions des éléments du HUD
        hudLeftX = hudVerticalMargin;
        hudRightX = WORLD_WIDTH * 2 / 3 - hudLeftX;
        hudCenterX = WORLD_WIDTH / 3;
        hudRow1Y = WORLD_HEIGHT - hudVerticalMargin;
        hudRow2Y = hudRow1Y - hudVerticalMargin - font.getCapHeight();
        hudSectionWidth = WORLD_WIDTH / 3;
    }


    @Override
    public void render(float delta) {
        batch.begin();

        // Vérifier si la santé du joueur est inférieure ou égale à 0
        if (player.health <= 0) {
            // Si oui, libérer les ressources et afficher l'écran de fin de partie
            this.dispose();
            game.setScreen(new GameOverScreen(game, score, WORLD_WIDTH, WORLD_HEIGHT));
            return;
        }

        // Rendu de l'arrière-plan
        renderBackground(delta);

        // Détection des entrées utilisateur
        detectInput(delta);

        // Mise à jour du joueur
        player.update(delta);

        // Itération sur les power-ups
        ListIterator<BoostEntity> pickUpsIterator = pickUps.listIterator();
        while (pickUpsIterator.hasNext()) {
            BoostEntity pickUp = pickUpsIterator.next();
            pickUp.move(delta);
            pickUp.draw(batch);
            // Suppression des power-ups hors de l'écran
            if (pickUp.thisRectangle.y + pickUp.thisRectangle.height < 0) {
                pickUpsIterator.remove();
            }
        }

        // Itération sur les ennemis
        ListIterator<Enemy> iterator = enemies.listIterator();
        while (iterator.hasNext()) {
            Enemy enemy = iterator.next();
            enemy.moveOnPattern(delta);
            enemy.update(delta);
            enemy.draw(batch);
        }

        // Rendu du joueur
        player.draw(batch);

        // Rendu des projectiles
        renderProjectiles(delta);

        // Apparition de nouveaux power-ups
        spawnPickUps(delta);

        // Détection des collisions
        detectCollisions();

        // Mise à jour et rendu de l'HUD (interface tête haute)
        updateAndRenderHUD();

        batch.end();
    }


    //met à jour et affiche les éléments de l'interface utilisateur (HUD) du jeu
    private void updateAndRenderHUD() {
        font.draw(batch, "Score", hudRightX, hudRow1Y, hudSectionWidth, Align.right, false);
        font.draw(batch, "Health", hudLeftX, hudRow1Y, hudSectionWidth, Align.left, false);
        font.draw(batch, String.format(Locale.getDefault(), "%06d", score), hudRightX, hudRow2Y,
                hudSectionWidth, Align.right, false);
        font.draw(batch, String.format(Locale.getDefault(), "%06d", player.health), hudLeftX, hudRow2Y,
                hudSectionWidth, Align.left, false);
    }


    //gère l'apparition des pick-ups dans le jeu
    private void spawnPickUps(float delta) {
        // Incrémente le compteur de spawn des power-ups
        powerUpSpawnTimer += delta;
        // Incrémente le compteur de spawn des bonus de santé
        healthUpSpawnTimer += delta;

        // Vérifie si le temps écoulé dépasse le délai nécessaire pour le spawn d'un power-up
        if (powerUpSpawnTimer > timeBetweenPowerUpSpawns) {
            // Ajoute un nouveau power-up à la liste des pickUps
            pickUps.add(new PowerUp(90f, 100 + SpaceShooterGame.random.nextFloat() * WORLD_WIDTH % (WORLD_WIDTH - 100),
                    WORLD_HEIGHT + 120f, 20, 20, powerUpPickUpTexture));
            // Réinitialise le compteur de spawn des power-ups
            powerUpSpawnTimer -= timeBetweenPowerUpSpawns;
        }

        // Vérifie si le temps écoulé dépasse le délai nécessaire pour le spawn d'un bonus de santé
        if (healthUpSpawnTimer > timeBetweenHealthUpSpawns) {
            // Ajoute un nouveau bonus de santé à la liste des pickUps
            pickUps.add(new HealthUp(80f, 100 + SpaceShooterGame.random.nextFloat() * WORLD_WIDTH % (WORLD_WIDTH - 100),
                    WORLD_HEIGHT + 120f, 20, 20, healthPickUpTexture));
            // Réinitialise le compteur de spawn des bonus de santé
            healthUpSpawnTimer -= timeBetweenHealthUpSpawns;
        }
    }


    //cette fonction responsable de la lecture des données pour la prochaine
    // vague d'ennemis à partir du fichier texte
    private void nextWave() throws IOException {
        reader.readLine(); // Ignorer la premiere ligne

        int quantity = Integer.parseInt(reader.readLine()); // lire le nbr d'enemies

        String enemyName = reader.readLine(); // lire le type d'ennemi
        String newName;
        float xPosition = 0, yPosition = 0;

        for (int i = 0; i < quantity; i++) {
            newName = reader.readLine(); // lire le nom d'enemi

            if (newName.equals("Next wave")) return; // si le nom est next wave on stop la lecture

            // verifie le type d'enemi ou la position x
            if (newName.equals("Small") || newName.equals("Medium") || newName.equals("Big")) {
                enemyName = newName;
                xPosition = Integer.parseInt(reader.readLine());
            } else {
                xPosition = Integer.parseInt(newName);
            }

            yPosition = Integer.parseInt(reader.readLine()); // lire la position y

            // creer des enemmies depuis ces critères
            if (enemyName.equals("Small")) {
                enemies.add(new SmallEnemy(120f, 5, xPosition,
                        yPosition, 22, 34, enemySmallTexture,
                        10f, 5f, 450f, 1.5f,
                        enemyProjectileTexture));
            } else if (enemyName.equals("Medium")) {
                enemies.add(new MediumEnemy(80f, 10, xPosition,
                        yPosition, 22, 34, ennemyBigTexture,
                        10f, 5f, 450f, 1.5f,
                        enemyProjectileTexture));
            } else if (enemyName.equals("Big")) {
                enemies.add(new BigEnemy(60f, 15, xPosition,
                        yPosition, 22, 34, enemyMediumTexture,
                        10f, 5f, 450f, 1.5f,
                        enemyProjectileTexture));
            }
        }
    }


    //deplcement du joueur
    private void detectInput(float delta) {
        // Limites de déplacement du joueur
        float leftLimit, rightLimit, upLimit, downLimit;
        leftLimit = -player.thisRectangle.x;
        downLimit = -player.thisRectangle.y;
        rightLimit = WORLD_WIDTH - player.thisRectangle.x - player.thisRectangle.width;
        upLimit = WORLD_HEIGHT - player.thisRectangle.y - player.thisRectangle.height;

        // Déplacement du joueur en fonction des touches du clavier
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && rightLimit > 0) {
            player.translate(Math.min(player.movementSpeed * delta, rightLimit), 0f);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP) && upLimit > 0) {
            player.translate(0f, Math.min(player.movementSpeed * delta, upLimit));
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && leftLimit < 0) {
            player.translate(Math.max(-player.movementSpeed * delta, leftLimit), 0f);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN) && downLimit < 0) {
            player.translate(0f, Math.max(-player.movementSpeed * delta, downLimit));
        }

        // Déplacement du joueur en fonction du toucher de l'écran
        if (Gdx.input.isTouched()) {
            float xTouchPosition = Gdx.input.getX();
            float yTouchPosition = Gdx.input.getY();

            // Convertir les coordonnées du toucher en coordonnées du jeu
            Vector2 touchPoint = new Vector2(xTouchPosition, yTouchPosition);
            touchPoint = viewport.unproject(touchPoint);

            // Calculer la distance entre le point de toucher et le centre du joueur
            Vector2 playerCenter = new Vector2(
                    player.thisRectangle.x + player.thisRectangle.width / 2,
                    player.thisRectangle.y + player.thisRectangle.height / 2);

            float touchDistance = touchPoint.dst(playerCenter);

            // Vérifier si la distance dépasse le seuil minimal pour déplacer le joueur
            if (touchDistance > TOUCH_THRESHOLD) {
                float xTouchDiff = touchPoint.x - playerCenter.x;
                float yTouchDiff = touchPoint.y - playerCenter.y;

                // Calculer les déplacements en x et y en fonction de la distance et de la vitesse de déplacement du joueur
                float xMove = xTouchDiff / touchDistance * player.movementSpeed * delta;
                float yMove = yTouchDiff / touchDistance * player.movementSpeed * delta;

                // Limiter les déplacements en fonction des limites de déplacement du joueur
                if (xMove > 0) {
                    xMove = Math.min(xMove, rightLimit);
                } else {
                    xMove = Math.max(xMove, leftLimit);
                }
                if (yMove > 0) {
                    yMove = Math.min(yMove, upLimit);
                } else {
                    yMove = Math.max(yMove, downLimit);
                }

                // Déplacer le joueur
                player.translate(xMove, yMove);
            }
        }
    }


    private void detectCollisions() {
        // Parcours des projectiles du joueur
        ListIterator<Projectile> iterator = playerProjectileList.listIterator();
        while (iterator.hasNext()) {
            Projectile projectile = iterator.next();

            // Parcours des ennemis
            ListIterator<Enemy> enemyListIterator = enemies.listIterator();
            while (enemyListIterator.hasNext()) {
                Enemy enemy = enemyListIterator.next();

                // Vérification de la collision entre le projectile et l'ennemi
                if (enemy.intersects(projectile.boundingBox)) {
                    // Vérification si l'ennemi est touché par le projectile
                    if (enemy.checkHit()) {
                        // Suppression de l'ennemi touché
                        enemyListIterator.remove();

                        // Actions spécifiques en fonction du type d'ennemi touché
                        switch (enemy.name) {
                            case "Small":
                                score += 69;
                                break;
                            case "Medium":
                                score += 228;
                                break;
                            case "Big":
                                score += 1488;
                                this.dispose(); // Suppression de l'instance de jeu actuelle
                                // Passage à l'écran de fin de jeu
                                game.setScreen(new GameCompletedScreen(game, score, WORLD_WIDTH, WORLD_HEIGHT));
                                return;
                        }

                        // Vérification si tous les ennemis ont été éliminés
                        if (enemies.isEmpty()) {
                            try {
                                nextWave(); // Passage à la vague suivante
                            } catch (IOException e) {
                                System.out.println(e.getMessage());
                            }
                        }
                    }

                    iterator.remove(); // Suppression du projectile du joueur
                    break; // Sortie de la boucle de parcours des ennemis
                }
            }
        }

        // Parcours des projectiles des ennemis
        iterator = enemyProjectileList.listIterator();
        while (iterator.hasNext()) {
            Projectile projectile = iterator.next();

            // Vérification de la collision entre le projectile ennemi et le joueur
            if (player.intersects(projectile.boundingBox)) {
                int before = player.health;
                player.checkHit(); // Vérification des dégâts causés par le projectile
                int after = player.health;

                // Réduction du score si le joueur a perdu de la vie
                if (before > after)
                    score -= 15;

                iterator.remove(); // Suppression du projectile ennemi
            }
        }

        // Parcours des objets de boost
        ListIterator<BoostEntity> pickUpsIterator = pickUps.listIterator();
        while (pickUpsIterator.hasNext()) {
            BoostEntity pickUp = pickUpsIterator.next();

            // Vérification de la collision entre le joueur et l'objet de boost
            if (player.intersects(pickUp.thisRectangle)) {
                pickUp.boosterAction(player); // Application de l'effet du boost sur le joueur
                pickUpsIterator.remove(); // Suppression de l'objet de boost
            }
        }
    }

    private void renderBackground(float deltaTime) {
        // Mise à jour des décalages des arrière-plans en fonction du temps écoulé
        backgroundOffsets[0] += deltaTime * bgMaxScrollingSpeed / 8;
        backgroundOffsets[1] += deltaTime * bgMaxScrollingSpeed / 4;
        backgroundOffsets[2] += deltaTime * bgMaxScrollingSpeed / 2;
        backgroundOffsets[3] += deltaTime * bgMaxScrollingSpeed;

        // Parcours des différentes couches d'arrière-plan
        for (int layer = 0; layer < backgroundOffsets.length; layer++) {
            // Vérification si le décalage dépasse la hauteur du monde
            if (backgroundOffsets[layer] > WORLD_HEIGHT)
                backgroundOffsets[layer] = 0; // Réinitialisation du décalage à 0

            // Rendu de l'arrière-plan à sa position actuelle
            batch.draw(backgrounds[layer], 0, -backgroundOffsets[layer],
                    WORLD_WIDTH, WORLD_HEIGHT);

            // Rendu de l'arrière-plan à sa position actuelle + hauteur du monde
            batch.draw(backgrounds[layer], 0, -backgroundOffsets[layer] + WORLD_HEIGHT,
                    WORLD_WIDTH, WORLD_HEIGHT);

            //En combinant ces deux rendus, l'effet de défilement parallaxe est obtenu,
            // où chaque couche d'arrière-plan semble se déplacer à une vitesse différente,
            // créant ainsi une sensation de profondeur et de mouvement dans le jeu.
        }
    }


    private void renderProjectiles(float delta) {
        if (player.canFire()) {
            Projectile[] projectiles = player.shootOnPattern();
            playerProjectileList.addAll(Arrays.asList(projectiles));
        }

        ListIterator<Enemy> enemyListIterator = enemies.listIterator();
        while (enemyListIterator.hasNext()) {
            Enemy enemy = enemyListIterator.next();
            if (enemy.canFire()) {
                Projectile[] projectiles = enemy.shootOnPattern();
                enemyProjectileList.addAll(Arrays.asList(projectiles));
            }
        }

        ListIterator<Projectile> iterator = playerProjectileList.listIterator();
        while (iterator.hasNext()) {
            Projectile projectile = iterator.next();
            projectile.draw(batch);
            projectile.boundingBox.y += projectile.yMovementSpeed * delta;
            //if (projectile.boundingBox.y > WORLD_HEIGHT)
            //iterator.remove();
        }

        iterator = enemyProjectileList.listIterator();
        while (iterator.hasNext()) {
            Projectile projectile = iterator.next();
            projectile.draw(batch);
            projectile.boundingBox.y -= projectile.yMovementSpeed * delta;
            projectile.boundingBox.x += projectile.xMovementSpeed * delta;
            // if (projectile.boundingBox.y + projectile.boundingBox.height < 0)
            //iterator.remove();
        }
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        batch.setProjectionMatrix(camera.combined);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void show() {
        backgroundMusic.play();
    }

    @Override
    public void dispose() {

    }
}
