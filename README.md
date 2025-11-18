# Projet Méthéo Android

Une application Android simple et épurée qui affiche les conditions météorologiques actuelles pour n'importe quelle ville dans le monde. L'application récupère les données en temps réel depuis l'API [OpenWeatherMap](https://openweathermap.org/api).

## 🚀 Fonctionnalités

*   **Recherche par ville** : Entrez le nom d'une ville pour obtenir ses informations météo.
*   **Données par défaut** : Affiche la météo d'Angers au lancement de l'application.
*   **Informations complètes** : Affiche un ensemble détaillé de données :
    *   Nom de la ville et coordonnées (latitude, longitude)
    *   Température actuelle
    *   Températures minimale et maximale
    *   Description du temps (ex: "Ciel dégagé", "Légère pluie")
    *   Pression atmosphérique (en hPa)
    *   Taux d'humidité (en %)
    *   Vitesse du vent (en km/h)
    *   Direction du vent (en degrés et point cardinal, ex: "270° (O)")
*   **Interface utilisateur claire** : Les données sont organisées de manière lisible pour une consultation rapide.
*   **Layouts adaptatifs** : L'interface s'adapte automatiquement pour les modes portrait et paysage.

## 🛠️ Technologies et Librairies utilisées

*   **Langage** : [Java](https://www.java.com/)
*   **Kit de développement** : Android SDK
*   **Requêtes réseau** : [Volley](https://developer.android.com/training/volley) - Une librairie HTTP pour des requêtes réseau rapides et efficaces.
*   **Composants d'interface** : Composants Android standards (`LinearLayout`, `GridLayout`, `EditText`, `Button`, `TextView`) avec des layouts XML.
*   **API de données** : [OpenWeatherMap Current Weather Data API](https://openweathermap.org/current)

## ⚙️ Configuration du projet

### Prérequis

*   Android Studio (dernière version recommandée)
*   Un appareil Android ou un émulateur configuré

### Installation

1.  **Ouvrir dans Android Studio**
    *   Lancez Android Studio.
    *   Sélectionnez "Open an existing Android Studio project".
    *   Naviguez jusqu'au dossier du projet et ouvrez-le.

2.  **Clé d'API OpenWeatherMap**
    L'application utilise une clé d'API pour accéder aux données d'OpenWeatherMap. La clé est actuellement codée en dur dans le fichier `MainActivity.java`.

    