# FoodInventory Android App

![FoodInventory Logo](app/src/main/res/mipmap-xxxhdpi/ic_launcher.png)

FoodInventory est une application Android développée en Kotlin qui vous aide à gérer votre
inventaire alimentaire. Suivez les articles de votre garde-manger, réfrigérateur ou congélateur, et
restez organisé avec cette application intuitive et facile à utiliser.

## Fonctionnalités

- **Récupération de données:** Récupère des données alimentaires depuis l'API Open Food Facts.
- **Intégration Supabase:** Utilise Supabase pour le stockage côté serveur et l'authentification.
- **Injection de dépendances Koin:** Implémente Koin pour l'injection de dépendances.
- **Interface utilisateur Jetpack Compose:** Utilise Jetpack Compose pour créer une interface
  utilisateur moderne et réactive.
- **Intégration CameraX:** Permet aux utilisateurs de capturer et de stocker des images d'articles
  alimentaires.
- **Scanner de code-barres:** Permet de scanner le code-barres d'un produit avec ML Kit
- **Chargement d'image Glide:** Intègre Glide pour le chargement efficace des images.
- **Journalisation Timber:** Implémente Timber pour des capacités de journalisation améliorées.

## Prérequis

Avant d'exécuter l'application, assurez-vous de configurer votre fichier `secrets.properties` avec
les propriétés suivantes :

```properties
SUPABASE_URL=<votre_url_supabase>
SUPABASE_KEY=<votre_clé_supabase>
APP_CENTER_KEY=<votre_clé_appcenter>
```

## Pour commencer

1. Clonez le dépôt :

   ```bash
   git clone https://github.com/your_username/FoodInventory.git
   ```

2. Ouvrez le projet dans Android Studio.

3. Créez un fichier `secrets.properties` dans le répertoire racine du projet et ajoutez votre URL et
   clé Supabase comme mentionné dans les prérequis.

4. Construisez et exécutez l'application sur un émulateur ou un appareil physique.

## Dépendances

- [Koin](https://insert-koin.io/): Pour l'injection de dépendances.
- [Retrofit](https://square.github.io/retrofit/): Pour les requêtes réseau.
- [Jetpack Compose](https://developer.android.com/jetpack/compose): Pour le développement de
  l'interface utilisateur.
- [CameraX](https://developer.android.com/training/camerax): Pour la fonctionnalité de la caméra.
- [Glide](https://github.com/bumptech/glide): Pour le chargement d'images.
- [Timber](https://github.com/JakeWharton/timber): Pour l'affichage des logs.
- [Supabase PostgREST-KT](https://github.com/jan-tennert/supabase-kt): Pour l'intégration avec
  Supabase.
- [Coil](https://coil-kt.github.io/coil/): Pour le chargement d'images dans Jetpack Compose.

## Contribution

Si vous souhaitez contribuer au projet, n'hésitez pas à piocher dans les issues du projet ou
contactez moi sur les réseaux sociaux.
