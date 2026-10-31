package pet.project.enums;

import pet.project.exceptions.NextPointAfterAdvantageException;

public enum Point {
    LOVE ("0"),
    FIFTEEN ("15"),
    THIRTY ("30"),
    FORTY ("40"),
    ADVANTAGE ("AD");

    // Больше подошло бы название stringValue
    private String title;

    Point(String title){
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public Point next(){
        return switch (this) {
            case LOVE -> FIFTEEN;
            case FIFTEEN -> THIRTY;
            case THIRTY -> FORTY;
            case FORTY -> ADVANTAGE;
            default -> throw new NextPointAfterAdvantageException(this);
        };
    }

}
