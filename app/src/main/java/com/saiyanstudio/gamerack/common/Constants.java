package com.saiyanstudio.gamerack.common;

/**
 * Created by deekshith on 12-11-2017.
 */

public class Constants {

    public static class SharedPrefTags {
        public static String gameSearch = "SHARED_PREFS_GAME_SEARCH";
        public static String gameSearchKey = "KEY_GAME_SEARCH";
        public static String apiCounter = "API_COUNTER";
    }

    public static class FragmentTags {
        public static String gameInfoDialogFragment = "FRAGMENT_GAME_INFO_DIALOG";
        public static String editGameDialogFragment = "FRAGMENT_EDIT_GAME_DIALOG";
    }

    public static class ViewPager {
        public static String allGames = "ALL";
        public static String tbpGames = "TBP";
        public static String completedGames = "COMPLETED";

        public static String baseGame = "BASE GAME";
        public static String expansion = "Expansion";
    }

    public static class IntentKeys {
        public static String game = "GAME";
        public static String addGame = "ADD_GAME";
        public static String editGame = "EDIT_GAME";
        public static String baseGame = "BASE_GAME";
        public static String expansion = "EXPANSION";
        public static String gameImage = "GAME_IMAGE_BITMAP";
        public static String imageId = "IMAGE_ID";
        public static String allGameList = "ALL_GAME_LIST";
        public static String tbpGameList = "TBP_GAME_LIST";
        public static String completedGameList = "COMPLETED_GAME_LIST";
        public static String statusFilter = "STATUS_FILTER";
        public static String backupFileName = "BACKUP_FILE_NAME";
        public static String restoreFileName = "RESTORE_FILE_NAME";
    }

    public static class IGDBApi {
        public static String baseUrl = "{ -- https://your-igdb-api-url.com -- }";
        public static String UserKey = "{ -- put-your-user-key-here -- }";
        public static String accept = "application/json";
        public static String coverBigImageBaseUrl = "https://images.igdb.com/igdb/image/upload/t_cover_big/%s.jpg";
        public static String coverFullScreenImageBaseUrl = "https://images.igdb.com/igdb/image/upload/t_1080p/%s.jpg";
    }

    public static class Platforms {
        public static String pc = "PC";
        public static String nintendo = "Nintendo";
        public static String playStation = "PlayStation";
        public static String xbox = "XBox";
        public static String mobile = "Mobile";
    }

    public static class Stores {
        public static String steam = "Steam";
        public static String gog = "GOG";
        public static String uPlay = "Uplay";
        public static String origin = "Origin";
        public static String pirated = "Pirated";
        public static String other = "Other";

        public static String store = "Store";
    }

    public static class PlatformAndStore {
        public static String platformAndStore = "%s/%s";
    }

    public static class Status {
        public static String all = "All";
        public static String toBePlayed = "To Be Played";
        public static String currentlyPlaying = "Currently Playing";
        public static String completed = "Completed";
    }

    public static class ContentRating {
        public static String esrbAndPegi = "ESRB-%s, PEGI-%s";
        public static String esrb = "ESRB-%s";
        public static String pegi = "PEGI-%s";
    }

    public static class AgeRatingCategory {
        public static int ESRB = 1;
        public static int PEGI = 13;
    }

    public static class AgeRatingValue {
        public static String Three = "3";
        public static String Seven = "7";
        public static String Twelve = "12";
        public static String Sixteen = "16";
        public static String Eighteen = "18";
        public static String RP = "RP";
        public static String EC = "EC";
        public static String E = "E";
        public static String E10 = "E10";
        public static String T = "T";
        public static String M = "M";
        public static String AO = "AO";
    }

    public static class WebSiteCategory {
        public static int OfficialSite = 1;
        public static int Steam = 13;
        public static int Twitch = 6;
        public static int Wikia = 2;
    }

    public static class Generic {
        public static String notAvailable = "N/A";
    }
}
