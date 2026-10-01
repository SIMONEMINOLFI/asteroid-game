# Gioco Asteroid

Un gioco Asteroid classico sviluppato con Java Swing secondo il pattern architetturale Model-View-Controller (MVC)

## Panoramica

Questo progetto implementa un gioco classico di Asteroid dove il giocatore controlla una navicella spaziale (posizionata inizialmente al centro dello schermo). La nave può ruotare, accelerare, o sparare, cercando di evitare e distruggere gli asteroidi in arrivo. Il gioco tiene traccia del punteggio e delle vite, aumentando di difficoltà man mano che il punteggio sale

## Istruzioni per Giocare

1. **Lanciare il gioco**: Eseguire Main.java per lanciare il videogioco
2. **Avviare del gioco**: Premere il pulsante "Start Game" dalla schermata principale
3. **Controlli**:
   - **Rotazione**: Frecce **SINISTRA/DESTRA** per ruotare la nave
   - **Propulsione**: Freccia **SU** per accelerare nella direzione corrente
   - **Sparo**: Tasto **SPAZIO** per sparare un proiettile
4. **Obiettivo**: Distruggere quanti più asteroidi possibile evitando le collisioni
5. **Punteggio**: 
   - **Asteroidi grandi**: 50 punti
   - **Asteroidi medi**: 30 punti
   - **Asteroidi piccoli**: 10 punti
6. **Power-up**: Raccogli i diamanti blu per attivare uno scudo temporaneo che protegge dagli asteroidi

## Meccaniche di Gioco

- La navicella parte dal centro dello schermo
- Gli asteroidi arrivano da tutte le direzioni
- Gli asteroidi grandi si dividono in medi, e i medi in piccoli quando colpiti
- La nave e i proiettili attraversano i bordi dello schermo (effetto wrapping).
- Ogni collisione con un asteroide riduce il numero di vite
- Il gioco termina quando le vite si esauriscono
- La velocità del gioco aumenta progressivamente ogni 500 punti

## Architettura

### Pattern MVC

Questo progetto segue rigorosamente il pattern architetturale Model-View-Controller (MVC):

1. **Model**: Gestisce i dati, la logica e le regole dell'applicazione
   - Rappresenta lo stato e la logica dell'applicazione
   - Indipendente dall'interfaccia utente
   - Gestisce direttamente i dati e le regole dell'applicazione

2. **View**: Renderizza il modello in un'interfaccia utente
   - Mostra i dati all'utente e gestisce l'interazione
   - Invia le azioni dell'utente al controller
   - Può interrogare il modello per lo stato ma non dovrebbe modificarlo direttamente

3. **Controller**: Agisce come intermediario tra Model e View
   - Riceve l'input dell'utente dalla View
   - Elabora le richieste aggiornando il Model
   - Seleziona quale View mostrare successivamente

### Architettura a Livelli

Il progetto implementa anche un'architettura a livelli:

1. **Livello di Presentazione**: Le viste (`HomeView`, `AsteroidGame`, `GameOverView`)
2. **Livello di Applicazione**: I controller (`HomeController`, `GameController`, `GameEngine`)
3. **Livello di Dominio**: I modelli (`Ship`, `Asteroid`, `Bullet`)
4. **Livello di Servizio**: I servizi (`GameService`, `AudioService`, `ScoreService`)

### Flow di Esecuzione

1. Main inizializza il frame principale e la HomeView
2. HomeController gestisce la navigazione tra le schermate
3. GameController coordina il gameplay con GameEngine
4. GameEngine gestisce gli aggiornamenti dello stato e delle collisioni
5. AsteroidGame renderizza gli elementi di gioco
6. GameOverController gestisce la fine del gioco e il riavvio

## Modello di Dominio

Il nostro modello di dominio è composto dalle seguenti classi principali:

### Entità di Gioco
- **Ship**: La navicella controllata dal giocatore con capacità di movimento e rotazione
- **Asteroid**: Gli asteroidi con dimensioni e velocità diverse (piccoli, medi, grandi)
- **Bullet**: I proiettili sparati dalla nave che possono distruggere gli asteroidi
- **PowerUp**: Elementi che forniscono abilità speciali come lo scudo

### Stato di Gioco
- **GameState**: Mantiene lo stato corrente del gioco (vite, punteggio, velocità)
- **Score**: Gestisce il punteggio e il punteggio massimo

### Relazioni Principali
- Ship spara Bullet
- Bullet colpisce Asteroid
- Asteroid può dividersi in Asteroid più piccoli
- Ship può collidere con Asteroid
- Ship può raccogliere PowerUp

## Scelte Implementative

### Interfacce e Classi Astratte

- **Runnable**: Utilizzato per i callback di game over e aggiornamenti
- **ActionListener**: Implementato per gestire gli eventi di timer nel GameEngine
- **KeyListener**: Implementato per gestire gli input utente in AsteroidGame

### Gerarchie

- **PowerUp**: Implementazione con tipi diversi (attualmente solo SHIELD)
- **Controller**: Gerarchia funzionale tra GameController, HomeController e GameOverController

### Design Patterns

- **MVC**: Principale pattern architetturale
- **Observer**: Utilizzato per gli aggiornamenti della view (via callbacks)
- **Strategy**: Utilizzato per gestire comportamenti diversi degli asteroidi
- **Factory**: Utilizzato in GameService per creare nuovi GameState

## Meccanismi e Algoritmi

### Fisica del Movimento

- **Movimento della Nave**: Implementato con vettori di velocità e accelerazione
  ```java
  // Fisica della nave
  velocityX += Math.cos(rotation) * acceleration;
  velocityY += Math.sin(rotation) * acceleration;
  velocityX *= friction; // Attrito per rallentare gradualmente
  velocityY *= friction;
  x += velocityX; // Aggiornamento posizione
  y += velocityY;
  ```

### Wrapping dello Schermo

- **Screen Wrapping**: Quando un oggetto esce da un bordo appare dal lato opposto
  ```java
  // Esempio di wrapping
  if (x < 0) x = screenWidth;
  if (x > screenWidth) x = 0;
  if (y < 0) y = screenHeight;
  if (y > screenHeight) y = 0;
  ```

### Divisione degli Asteroidi

- **Algoritmo di Splitting**: Quando un asteroide grande viene colpito, si divide in due medi
  ```java
  // Al momento dell'impatto
  if (asteroid.getSize() == 3) { // Grande -> due medi
      // Crea due asteroidi medi con direzioni casuali
  } else if (asteroid.getSize() == 2) { // Medio -> due piccoli
      // Crea due asteroidi piccoli con direzioni casuali
  }
  ```

### Rilevamento Collisioni

- **Collisioni**: Utilizzo di rettangoli di delimitazione per rilevare le collisioni
  ```java
  bullet.getBounds().intersects(asteroid.getBounds())
  ```

## Librerie Esterne Utilizzate

- **Java Swing**: Framework UI per GUI desktop
  - JFrame, JPanel, JButton, JLabel per l'interfaccia utente
  - Timer per il game loop
  - KeyListener per l'input utente

- **Java Sound API**: Per gli effetti sonori
  - Utilizzata nella classe `AudioService` per riprodurre suoni di gioco
  - Già inclusa nel JDK standard, non richiede dipendenze aggiuntive
  - Esempio di utilizzo:
  ```java
  // In AudioService.java
  private void playSound(String soundFile) {
      try {
          URL soundURL = getClass().getResource(soundFile);
          AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundURL);
          Clip clip = AudioSystem.getClip();
          clip.open(audioIn);
          clip.start();
      } catch (Exception e) {
          e.printStackTrace();
      }
  }
  ```

- **Java 2D API**: Per il rendering grafico
  - Utilizzata in `AsteroidGame` per il rendering avanzato
  - Già inclusa nel JDK standard, non richiede dipendenze aggiuntive
  - Esempio di utilizzo:
  ```java
  // In AsteroidGame.java durante il rendering della nave
  Graphics2D g2d = (Graphics2D) g.create();
  g2d.translate(ship.getX(), ship.getY());
  g2d.rotate(ship.getRotation());
  // Disegno della nave con rotazione
  g2d.dispose();
  ```

**Nota**: Entrambe queste API sono parte del JDK standard di Java e non richiedono aggiunte al file pom.xml. Sono utilizzate nativamente nel codice

## Componenti Grafici

### Elementi dell'Interfaccia

- **HomeView**: Schermata iniziale con menu
  - JLabel per titoli e istruzioni
  - JButton per navigazione (Start, Settings, Exit)

- **GamePanel**: Contenitore di gioco
  - BorderLayout per organizzare il pannello di gioco
  - JLabel per visualizzare il punteggio

- **AsteroidGame**: Pannello di rendering principale
  - Grafica personalizzata per la nave e gli asteroidi
  - Effetti visivi per lo scudo e la propulsione

- **GameOverView**: Schermata di fine gioco
  - JLabel per mostrare il punteggio finale
  - JButton per riavviare o tornare al menu

### Tecniche di Rendering

- **Sprite Rotation**: Rotazione della nave tramite trasformazioni 2D
- **Scaling**: Dimensionamento degli asteroidi in base alla dimensione
- **Custom Drawing**: Rendering personalizzato con Graphics2D

## Screenshot Salienti

1. **Schermata Principale**: Mostra il menu principale con opzioni di gioco
2. **Gameplay**: Mostra la nave in azione, asteroidi e proiettili
3. **Shield Attivo**: Mostra la nave con lo scudo attivato
4. **Game Over**: Mostra la schermata di fine gioco con il punteggio

## Struttura del Progetto

Il progetto è organizzato nei seguenti pacchetti principali:

```
asteroid/
├── controller/      # Controllers that handle application logic
│   ├── game/        # Game-specific controllers
│   ├── HomeController.java
│   └── GameOverController.java
├── model/           # Data models that represent game state
│   ├── game/        # Game entity models
│   ├── GameState.java
│   └── Score.java
├── services/        # Business logic services
│   ├── GameService.java
│   └── AsteroidService.java
├── view/            # UI components and game rendering
│   ├── game/        # Game screen views
│   ├── AsteroidGame.java
│   ├── HomeView.java
│   └── GameOverView.java
└── Main.java        # Application entry point
```

## Descrizione dei Componenti

### Modelli

- **GameState.java**: Rappresenta lo stato corrente del gioco, inclusi punteggio, vite e velocità del gioco
- **Score.java**: Gestisce il punteggio del giocatore e il punteggio più alto
- **Ship.java**: Rappresenta la navicella del giocatore con i dati di posizione
- **Asteroid.java**: Rappresenta un asteroide con posizione, dimensione e logica di movimento
- **Bullet.java**: Rappresenta i proiettili (per implementazione futura)

### Viste

- **HomeView.java**: La schermata iniziale con le opzioni di gioco
- **GamePanel.java**: Contenitore per il gioco principale che gestisce i componenti dell'interfaccia utente
- **AsteroidGame.java**: Il componente principale di rendering del gioco che visualizza gli elementi del gioco
- **GameOverView.java**: La schermata di game over mostrata quando il giocatore perde

### Controller

- **HomeController.java**: Gestisce la navigazione dalla schermata iniziale
- **GameController.java**: Controller principale che collega lo stato del gioco con la vista del gioco
- **GameOverController.java**: Gestisce la funzionalità di riavvio del gioco
- **AsteroidController.java**: Gestisce la logica specifica degli asteroidi

### Servizi

- **GameService.java**: Gestisce la logica del gioco come l'aggiornamento del punteggio e delle vite
- **AsteroidService.java**: Servizi relativi al comportamento degli asteroidi

### Punto di Ingresso

- **Main.java**: Punto di ingresso dell'applicazione che inizializza il gioco