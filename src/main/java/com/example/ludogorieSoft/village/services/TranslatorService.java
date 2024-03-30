package com.example.ludogorieSoft.village.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
@Service
@AllArgsConstructor
public class TranslatorService {
    private static final Map<Character, String> CYRILLIC_TO_LATIN_MAP = new HashMap<>();

    static {
        CYRILLIC_TO_LATIN_MAP.put('а', "a");
        CYRILLIC_TO_LATIN_MAP.put('б', "b");
        CYRILLIC_TO_LATIN_MAP.put('в', "v");
        CYRILLIC_TO_LATIN_MAP.put('г', "g");
        CYRILLIC_TO_LATIN_MAP.put('д', "d");
        CYRILLIC_TO_LATIN_MAP.put('е', "e");
        CYRILLIC_TO_LATIN_MAP.put('ж', "zh");
        CYRILLIC_TO_LATIN_MAP.put('з', "z");
        CYRILLIC_TO_LATIN_MAP.put('и', "i");
        CYRILLIC_TO_LATIN_MAP.put('й', "y");
        CYRILLIC_TO_LATIN_MAP.put('к', "k");
        CYRILLIC_TO_LATIN_MAP.put('л', "l");
        CYRILLIC_TO_LATIN_MAP.put('м', "m");
        CYRILLIC_TO_LATIN_MAP.put('н', "n");
        CYRILLIC_TO_LATIN_MAP.put('о', "o");
        CYRILLIC_TO_LATIN_MAP.put('п', "p");
        CYRILLIC_TO_LATIN_MAP.put('р', "r");
        CYRILLIC_TO_LATIN_MAP.put('с', "s");
        CYRILLIC_TO_LATIN_MAP.put('т', "t");
        CYRILLIC_TO_LATIN_MAP.put('у', "u");
        CYRILLIC_TO_LATIN_MAP.put('ф', "f");
        CYRILLIC_TO_LATIN_MAP.put('х', "h");
        CYRILLIC_TO_LATIN_MAP.put('ц', "ts");
        CYRILLIC_TO_LATIN_MAP.put('ч', "ch");
        CYRILLIC_TO_LATIN_MAP.put('ш', "sh");
        CYRILLIC_TO_LATIN_MAP.put('щ', "sht");
        CYRILLIC_TO_LATIN_MAP.put('ъ', "a");
        CYRILLIC_TO_LATIN_MAP.put('ь', "y");
        CYRILLIC_TO_LATIN_MAP.put('ю', "yu");
        CYRILLIC_TO_LATIN_MAP.put('я', "ya");
        CYRILLIC_TO_LATIN_MAP.put('А', "A");
        CYRILLIC_TO_LATIN_MAP.put('Б', "B");
        CYRILLIC_TO_LATIN_MAP.put('В', "V");
        CYRILLIC_TO_LATIN_MAP.put('Г', "G");
        CYRILLIC_TO_LATIN_MAP.put('Д', "D");
        CYRILLIC_TO_LATIN_MAP.put('Е', "E");
        CYRILLIC_TO_LATIN_MAP.put('Ж', "Zh");
        CYRILLIC_TO_LATIN_MAP.put('З', "Z");
        CYRILLIC_TO_LATIN_MAP.put('И', "I");
        CYRILLIC_TO_LATIN_MAP.put('Й', "Y");
        CYRILLIC_TO_LATIN_MAP.put('К', "K");
        CYRILLIC_TO_LATIN_MAP.put('Л', "L");
        CYRILLIC_TO_LATIN_MAP.put('М', "M");
        CYRILLIC_TO_LATIN_MAP.put('Н', "N");
        CYRILLIC_TO_LATIN_MAP.put('О', "O");
        CYRILLIC_TO_LATIN_MAP.put('П', "P");
        CYRILLIC_TO_LATIN_MAP.put('Р', "R");
        CYRILLIC_TO_LATIN_MAP.put('С', "S");
        CYRILLIC_TO_LATIN_MAP.put('Т', "T");
        CYRILLIC_TO_LATIN_MAP.put('У', "U");
        CYRILLIC_TO_LATIN_MAP.put('Ф', "F");
        CYRILLIC_TO_LATIN_MAP.put('Х', "H");
        CYRILLIC_TO_LATIN_MAP.put('Ц', "Ts");
        CYRILLIC_TO_LATIN_MAP.put('Ч', "Ch");
        CYRILLIC_TO_LATIN_MAP.put('Ш', "Sh");
        CYRILLIC_TO_LATIN_MAP.put('Щ', "Sht");
        CYRILLIC_TO_LATIN_MAP.put('Ъ', "A");
        CYRILLIC_TO_LATIN_MAP.put('Ь', "Y");
        CYRILLIC_TO_LATIN_MAP.put('Ю', "Yu");
        CYRILLIC_TO_LATIN_MAP.put('Я', "Ya");
    }

    public String translateToLatin(String cyrillicName) {
        StringBuilder latinNameBuilder = new StringBuilder();
        for (char ch : cyrillicName.toCharArray()) {
            String latinEquivalent = CYRILLIC_TO_LATIN_MAP.getOrDefault(ch, String.valueOf(ch));
            latinNameBuilder.append(latinEquivalent);
        }
        return latinNameBuilder.toString();
    }
}
