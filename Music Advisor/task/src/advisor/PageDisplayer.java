package advisor;

import java.util.ArrayList;
import java.util.List;

public class PageDisplayer {
    private int totalPageNum;
    private int entriesPerPage;
    private int currentPageIndex;
    private List<List<String>> pages;

    public PageDisplayer(int entriesPerPage) {
        this.entriesPerPage = entriesPerPage;
        this.currentPageIndex = 0;

    }

    public void createEmptyPages() {
        this.currentPageIndex = 0;
        this.pages = new ArrayList<>();
        for (int i = 0; i < this.totalPageNum; i++) {
            pages.add(new ArrayList<String>());
        }
    }
    public void setPageContent(List<String> content) {
        totalPageNum = content.size() / entriesPerPage;
        createEmptyPages();
        int count = 0;


        for (int i = 0; i < totalPageNum; i++) {
            // add content to each page
            for (int j = 0; j < entriesPerPage; j++) {
                if (count < content.size()) {
                    pages.get(i).add(content.get(count));
                    count++;
                }
            }
        }


    }

    public void printCurrentPage() {

        for (int i = 0; i < pages.get(currentPageIndex).size(); i++) {
            System.out.println(pages.get(currentPageIndex).get(i));
        }

        System.out.println(String.format("---PAGE %d OF %d---", currentPageIndex + 1, totalPageNum));
        // because the page number to print to the console starts at 1
    }

    public void printPreviousPage() {
        if (currentPageIndex <= 0) {
            System.out.println("No more pages.");
        } else {
            currentPageIndex--;
            printCurrentPage();
        }
    }

    public void printNextPage() {
        if (currentPageIndex >= totalPageNum - 1) {
            System.out.println("No more pages.");
        } else {
            currentPageIndex++;
            printCurrentPage();
        }
    }
}
