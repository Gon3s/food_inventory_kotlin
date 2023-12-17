# FoodInventory Android App

![FoodInventory Logo](app/src/main/res/mipmap-xxxhdpi/ic_launcher.png)

FoodInventory est une application Android développée en Kotlin qui vous aide à gérer votre inventaire alimentaire. Suivez les articles de votre garde-manger, réfrigérateur ou congélateur, et restez organisé avec cette application intuitive et facile à utiliser.

## Fonctionnalités

- **Récupération de données:** Récupère des données alimentaires depuis l'API Open Food Facts.
- **Intégration Supabase:** Utilise Supabase pour le stockage côté serveur et l'authentification.
- **Injection de dépendances Koin:** Implémente Koin pour l'injection de dépendances.
- **Interface utilisateur Jetpack Compose:** Utilise Jetpack Compose pour créer une interface utilisateur moderne et réactive.
- **Intégration CameraX:** Permet aux utilisateurs de capturer et de stocker des images d'articles alimentaires.
- **Base de données Room:** Utilise Room pour le stockage local de la base de données.
- **Scanner de code-barres ZXing:** Permet la numérisation rapide des articles.
- **Chargement d'image Glide:** Intègre Glide pour le chargement efficace des images.
- **Journalisation Timber:** Implémente Timber pour des capacités de journalisation améliorées.

## Prérequis

Avant d'exécuter l'application, assurez-vous de configurer votre fichier `secrets.properties` avec les propriétés suivantes :

```properties
SUPABASE_URL=<votre_url_supabase>
SUPABASE_KEY=<votre_clé_supabase>
```

## Pour commencer

1. Clonez le dépôt :

   ```bash
   git clone https://github.com/your_username/FoodInventory.git
   ```

2. Ouvrez le projet dans Android Studio.

3. Créez un fichier `secrets.properties` dans le répertoire racine du projet et ajoutez votre URL et clé Supabase comme mentionné dans les prérequis.

4. Construisez et exécutez l'application sur un émulateur ou un appareil physique.

## Dépendances

- [Koin](https://insert-koin.io/): Version 3.5.0 pour l'injection de dépendances.
- [Retrofit](https://square.github.io/retrofit/): Version 2.9.0 pour les requêtes réseau.
- [Jetpack Compose](https://developer.android.com/jetpack/compose): Version 1.2.0-alpha12 pour le développement de l'interface utilisateur.
- [CameraX](https://developer.android.com/training/camerax): Version 1.3.1 pour la fonctionnalité de la caméra.
- [Room](https://developer.android.com/training/data-storage/room): Version 2.6.1 pour le stockage local de la base de données.
- [ZXing Android Embedded](https://github.com/journeyapps/zxing-android-embedded): Version 4.3.0 pour la numérisation des codes-barres.
- [Glide](https://github.com/bumptech/glide): Version 4.16.0 pour le chargement d'images.
- [Timber](https://github.com/JakeWharton/timber): Version 5.0.1 pour la journalisation.
- [Supabase PostgREST-KT](https://github.com/jan-tennert/supabase-kt): Version 2.0.0 pour l'intégration avec Supabase.
- [Coil](https://coil-kt.github.io/coil/): Version 2.5.0 pour le chargement d'images dans Jetpack Compose.

## Contribution

Si vous souhaitez contribuer au projet, veuillez suivre les [directives de contribution](CONTRIBUTING.md).

## Licence

Ce projet est sous licence MIT - consultez le fichier [LICENSE](LICENSE) pour plus de détails.
