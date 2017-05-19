package com.app.maththpt.model;

import android.graphics.Typeface;
import android.widget.TextView;

import java.util.Comparator;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;

/**
 * Created by FRAMGIA\nguyen.duc.manh on 16/02/2017.
 */

public class Tests extends RealmObject {
    @PrimaryKey
    public int id;
    public String displayname;
    public String author;
    @Ignore
    public boolean isNew;
    public boolean isSeen;
    public boolean isCompleted;
    public Tests() {
        isSeen = false;
    }

    public Tests(int id, String name, String author) {
        this.id = id;
        this.displayname = name;
        this.author = author;
        isSeen = false;
    }

    public Tests(String name, String author) {
        this.displayname = name;
        this.author = author;
        isSeen = false;
    }

    @android.databinding.BindingAdapter("android:typeface")
    public static void setTypeface(TextView v, String style) {
        switch (style) {
            case "bold":
                v.setTypeface(null, Typeface.BOLD);
                break;
            default:
                v.setTypeface(null, Typeface.NORMAL);
                break;
        }
    }

    public enum TestsComparator implements Comparator<Tests> {
        ID_SORT {
            public int compare(Tests o1, Tests o2) {
                return Integer.valueOf(o1.id).compareTo(o2.id);
            }
        },
        SEEN_SORT {
            public int compare(Tests o1, Tests o2) {
                Integer a, b;
                if (o1.isSeen) {
                    a = 1;
                } else {
                    a = 0;
                }
                if (o2.isSeen) {
                    b = 1;
                } else {
                    b = 0;
                }
                return b.compareTo(a);
            }
        };

        public static Comparator<Tests> decending(final Comparator<Tests> other) {
            return (o1, o2) -> -1 * other.compare(o1, o2);
        }

        public static Comparator<Tests> getComparator(final TestsComparator... multipleOptions) {
            return (o1, o2) -> {
                for (TestsComparator option : multipleOptions) {
                    int result = option.compare(o1, o2);
                    if (result != 0) {
                        return result;
                    }
                }
                return 0;
            };
        }
    }
}
