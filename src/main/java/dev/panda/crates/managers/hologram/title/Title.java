/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package dev.panda.crates.managers.hologram.title;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Setter
@Getter
public class Title {
    private String title;
    private String subtitle;
    private int fadeIn;
    private int stay;
    private int fadeOut;

    Title(String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        this.title = title;
        this.subtitle = subtitle;
        this.fadeIn = fadeIn;
        this.stay = stay;
        this.fadeOut = fadeOut;
    }

    public static TitleBuilder builder() {
        return new TitleBuilder();
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Title)) {
            return false;
        }
        Title other = (Title)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getFadeIn() != other.getFadeIn()) {
            return false;
        }
        if (this.getStay() != other.getStay()) {
            return false;
        }
        if (this.getFadeOut() != other.getFadeOut()) {
            return false;
        }
        String this$title = this.getTitle();
        String other$title = other.getTitle();
        if (!Objects.equals(this$title, other$title)) {
            return false;
        }
        String this$subtitle = this.getSubtitle();
        String other$subtitle = other.getSubtitle();
        return !(!Objects.equals(this$subtitle, other$subtitle));
    }

    protected boolean canEqual(Object other) {
        return other instanceof Title;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getFadeIn();
        result = result * 59 + this.getStay();
        result = result * 59 + this.getFadeOut();
        String $title = this.getTitle();
        result = result * 59 + ($title == null ? 43 : $title.hashCode());
        String $subtitle = this.getSubtitle();
        result = result * 59 + ($subtitle == null ? 43 : $subtitle.hashCode());
        return result;
    }

    public String toString() {
        return "Title(title=" + this.getTitle() + ", subtitle=" + this.getSubtitle() + ", fadeIn=" + this.getFadeIn() + ", stay=" + this.getStay() + ", fadeOut=" + this.getFadeOut() + ")";
    }

    public static class TitleBuilder {
        private String title;
        private String subtitle;
        private int fadeIn;
        private int stay;
        private int fadeOut;

        TitleBuilder() {
        }

        public TitleBuilder title(String title) {
            this.title = title;
            return this;
        }

        public TitleBuilder subtitle(String subtitle) {
            this.subtitle = subtitle;
            return this;
        }

        public TitleBuilder fadeIn(int fadeIn) {
            this.fadeIn = fadeIn;
            return this;
        }

        public TitleBuilder stay(int stay) {
            this.stay = stay;
            return this;
        }

        public TitleBuilder fadeOut(int fadeOut) {
            this.fadeOut = fadeOut;
            return this;
        }

        public Title build() {
            return new Title(this.title, this.subtitle, this.fadeIn, this.stay, this.fadeOut);
        }

        public String toString() {
            return "Title.TitleBuilder(title=" + this.title + ", subtitle=" + this.subtitle + ", fadeIn=" + this.fadeIn + ", stay=" + this.stay + ", fadeOut=" + this.fadeOut + ")";
        }
    }
}

