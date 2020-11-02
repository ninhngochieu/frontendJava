package Home.DTO;

public class Page {
    private int  current_page = 1;
    private int total;
    private int total_page;

    public Page(int current_page, int total, int total_page) {
        this.current_page = current_page;
        this.total = total;
        this.total_page = total_page;
    }
    public Page(){

    }
    public int getCurrent_page() {
        return current_page;
    }

    public void setCurrent_page(int current_page) {
        this.current_page = current_page;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getTotal_page() {
        return total_page;
    }

    public void setTotal_page(int total_page) {
        this.total_page = total_page;
    }
}
