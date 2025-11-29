package vn.hn.hncommonservice.utils;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.util.regex.Pattern;

public final class CoreUtils {
	private static final Pattern DIACRITICS_AND_FRIENDS = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
	
	private CoreUtils() {
	}
	
	public static String removeAccent(String s) {
		if (s == null) {
			return "";
		} else {
			String nfd = Normalizer.normalize(s, Form.NFD);
			String withoutDiacritics = DIACRITICS_AND_FRIENDS.matcher(nfd).replaceAll("");
			String result = withoutDiacritics.replace('Đ', 'D').replace('đ', 'd');
			return result.toLowerCase().trim();
		}
	}
	
	public static PageRequest getPageRequest(int page, int size, String sortBy, String sortDir) {
		Sort.Direction direction;
		if ("ASC".equals(sortDir)) {
			direction = Direction.ASC;
		} else {
			direction = Direction.DESC;
		}
		
		if (page < 0) {
			page = 0;
		}
		
		if (size < 0) {
			size = 20;
		}
		
		if (size > 5000) {
			size = 5000;
		}
		
		return PageRequest.of(page, size, direction, new String[]{sortBy});
	}
}
