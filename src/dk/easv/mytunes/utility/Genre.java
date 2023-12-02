package dk.easv.mytunes.utility;

public enum Genre {
    ROCK("Rock"),ELECTRONIC("Electronic"),DANCE("Dance"),JAZZ("Jazz"),BASS("Bass"),INSTRUMENTAL("Instrumental"),
    TRI_HOP("Trip-Hop"),HIP_HOP("Hip-Hop"),CLASSIC("Classic"),BLUES("Blues"),HEAVY_METAL("Heavy-Metal"),
    POP("Pop"),PUNK("Punk"),REGGAE("Reggae");
private final String genre;
    Genre(String genre) {
    this.genre=genre;
    }

    public String getGenre() {
        return genre;
    }
}
