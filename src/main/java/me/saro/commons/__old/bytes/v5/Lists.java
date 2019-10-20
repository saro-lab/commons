package me.saro.commons.__old.bytes.v5;

import java.util.Comparator;
import java.util.List;
import java.util.function.BiConsumer;

/**
 *
 * @author  PARK Yong Seo
 * @since   5.0
 */
public class Lists {

    public static enum SortOption {
        none,
        asc,
        desc
    }


    /**
     * bind rank
     * @param list
     * @param sortOption
     * @param comparator
     * @param bindRank
     * @param <T>
     * @return
     */
    public static <T> List<T> bindRank(List<T> list, SortOption sortOption, Comparator<T> comparator, BiConsumer<T, Integer> bindRank) {

        if (list.isEmpty()) {
            return list;
        }

        // sort
        switch (sortOption != null ? sortOption : SortOption.none) {
            case asc:
                list.sort(comparator);
                break;
            case desc:
                list.sort((a, b) -> -comparator.compare(a, b));
        }

        int rank = 1;
        int rankAcc = 1;
        int index = 0;
        T prev = list.get(0);
        bindRank.accept(prev, rank);
        while(++index < list.size()) {
            T node = list.get(index);
            if (comparator.compare(prev, node) == 0) {
                rankAcc++;
            } else {
                rank += rankAcc;
                rankAcc = 1;
            }
            bindRank.accept(node, rank);
            prev = node;
        }
        return list;
    }
}
