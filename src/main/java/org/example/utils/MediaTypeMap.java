package org.example.utils;


import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.http.InvalidMediaTypeException;
import org.springframework.http.MediaType;


import java.util.*;
import java.util.stream.Collectors;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MediaTypeMap implements IStringToMediaTypeConverter {

    Map<String, MediaType> str_media_map;
    Map<String, List<String>> str_media_aliases;



    public MediaTypeMap() {
        str_media_map = new TreeMap<>();
        str_media_aliases = new HashMap<>();
        List<String> alias_types = null;
        Map<String, Object> media_static_fields_and_vals = ClassHelper.getPublicStaticFieldsWithValues(MediaType.class);
        for(Map.Entry<String, Object> fieldVal : media_static_fields_and_vals.entrySet()) {
            if (fieldVal.getValue() instanceof String) {

                //only fields with '_VALUE' suffix has string type.
                String key = (String) fieldVal.getValue();
                Object val = media_static_fields_and_vals.get(StringHelper.subtractFrom(fieldVal.getKey(), "_VALUE"));
                str_media_map.put(key, (MediaType) val); //type/subtype => val.

                String alias = key.substring(key.lastIndexOf('/') + 1); //subtype
                alias_types = str_media_aliases.computeIfAbsent(alias, k -> new ArrayList<>());
                alias_types.add(key);
            }
            else if(fieldVal.getValue() instanceof MediaType){
                MediaType val = ((MediaType) fieldVal.getValue());
                str_media_map.put(fieldVal.getKey(), val);
                String alias = val.getSubtype(); //subtype.
                alias_types = str_media_aliases.computeIfAbsent(alias, k -> new ArrayList<>());
                alias_types.add(val.getType() + "/" + val.getSubtype());
            }
        }
    }

    public List<MediaType> getMedias(String medias, MediaType def){
        List<MediaType> result = new ArrayList<>();
        for(String media: medias.split(",")){
            getMedia(result, media, def);
        }
        return result;
    }

    public void getMedia(List<MediaType> result, String key, MediaType def){
        MediaType one = str_media_map.get(key);

        //1. Extract by key.
        if(one != null)
            result.add(one);

        //2. Extract by alias.
        else if(str_media_aliases.get(key) != null){
            result.addAll(
                    str_media_aliases.get(key)
                            .stream()
                            .map(x -> str_media_map.getOrDefault(x, def))
                            .filter(Objects::nonNull)
                            .collect(Collectors.toList())
            );
        }
        else
            //3. Parse raw media type with parameters.
            try
            {
                result.add(MediaType.parseMediaType(key));
            }
            catch (InvalidMediaTypeException e)
            {
                //4. Return default media type if present.
                if(def != null)
                    result.add(def);
            }
    }

    public MediaType getFirstByQualityOrDefault(String key, MediaType def){
        List<MediaType> result = getMedias(key, def);
        MediaType.sortByQualityValue(result);
        if(result.size() > 0)
            return result.get(0);
        else
            return null;
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("mapping: ").append("\n{");
        for(Map.Entry<String, MediaType> kv : str_media_map.entrySet()){
            sb.append('\t').append('\"').append(kv.getKey()).append('\"').append(": ").append(kv.getValue()).append('\n');
        }
        sb.append("}\n");
        sb.append("aliases: ").append("\n{");
        for(Map.Entry<String, List<String>> kv : str_media_aliases.entrySet()){
            sb.append('\t').append('\"').append(kv.getKey()).append('\"').append(": ").append(StringHelper.iterableToString(kv.getValue())).append('\n');
        }
        sb.append('}');
        return sb.toString();
    }
}