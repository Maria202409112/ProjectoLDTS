package anaconda.models;

public class MenuModel {
    private int selectedOption;
    private final String[] options;

    public MenuModel() {
        this.selectedOption = 0;
        this.options = new String[] {
                "INICIAR JOGO",
                "INSTRUÇÕES",
                "SAIR"
        };
    }

    public void moveUp() {
        selectedOption = (selectedOption - 1 + options.length) % options.length;
    }

    public void moveDown() {
        selectedOption = (selectedOption + 1) % options.length;
    }

    public int getSelectedOption() {
        return selectedOption;
    }

    public String[] getOptions() {
        return options;
    }

    public int getOptionsCount() {
        return options.length;
    }

    public void resetSelection() {
        selectedOption = 0;
    }
}