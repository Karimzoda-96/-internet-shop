package my.shop.azhar.util;

import java.text.Normalizer;
import java.util.Locale;
import java.util.Map;

public final class SlugUtil {
    private static final Map<Character, String> CYRILLIC = Map.ofEntries(
            Map.entry('а', "a"), Map.entry('б', "b"), Map.entry('в', "v"), Map.entry('г', "g"),
            Map.entry('д', "d"), Map.entry('е', "e"), Map.entry('ё', "e"), Map.entry('ж', "zh"),
            Map.entry('з', "z"), Map.entry('и', "i"), Map.entry('й', "y"), Map.entry('к', "k"),
            Map.entry('л', "l"), Map.entry('м', "m"), Map.entry('н', "n"), Map.entry('о', "o"),
            Map.entry('п', "p"), Map.entry('р', "r"), Map.entry('с', "s"), Map.entry('т', "t"),
            Map.entry('у', "u"), Map.entry('ф', "f"), Map.entry('х', "h"), Map.entry('ц', "ts"),
            Map.entry('ч', "ch"), Map.entry('ш', "sh"), Map.entry('щ', "sch"), Map.entry('ы', "y"),
            Map.entry('э', "e"), Map.entry('ю', "yu"), Map.entry('я', "ya")
    );

    private SlugUtil() {
    }

    public static String toSlug(String value) {
        String lower = value == null ? "" : value.trim().toLowerCase(Locale.ROOT);
        StringBuilder transliterated = new StringBuilder();
        for (char c : lower.toCharArray()) {
            transliterated.append(CYRILLIC.getOrDefault(c, String.valueOf(c)));
        }
        String normalized = Normalizer.normalize(transliterated, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "");
        String slug = normalized.replaceAll("[^a-z0-9]+", "-")
                .replaceAll("(^-|-$)", "");
        return slug.isBlank() ? "item" : slug;
    }
}
