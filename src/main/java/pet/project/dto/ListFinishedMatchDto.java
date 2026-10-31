package pet.project.dto;

import java.util.List;

public class ListFinishedMatchDto {

    // Этот класс можно сделать record-ом. Это значительно сократит шаблонный код.

    // В объектах DTO лучше не иметь сеттеров без крайней необходимости (которой в этом проекте нет),
        // чтобы не было возможности забыть установить одно из полей.

    private List<FinishedMatchDto> matches;
    private int currentPage;
    private long totalPages;

    public ListFinishedMatchDto() {}

    public ListFinishedMatchDto(List<FinishedMatchDto> matches, int currentPage, long totalPages) {
        this.matches = matches;
        this.currentPage = currentPage;
        this.totalPages = totalPages;
    }

    public List<FinishedMatchDto> getMatches() {
        return matches;
    }

    public void setMatches(List<FinishedMatchDto> matches) {
        this.matches = matches;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public long getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(long totalPage) {
        this.totalPages = totalPage;
    }
}
