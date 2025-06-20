package week_8.TypeRacer;

public class Typer extends Thread {
    private String botName, wordsTyped;
    private double wpm;
    private TypeRacer typeRacer;

    public Typer(String botName, double wpm, TypeRacer typeRacer) {
        this.botName = botName;
        this.wpm = wpm;
        this.wordsTyped = "";
        this.typeRacer = typeRacer;
    }

    public void setBotName(String botName) {
        this.botName = botName;
    }

    public void setWpm(double wpm) {
        this.wpm = wpm;
    }

    public void addWordsTyped(String newWordsTyped) {
        this.wordsTyped += newWordsTyped + " ";
    }

    public String getWordsTyped() {
        return wordsTyped;
    }

    public String getBotName() {
        return botName;
    }

    public double getWpm() {
        return wpm;
    }

    @Override
    public void run() {
        String[] wordsToType = typeRacer.getWordsToType().split(" ");
        double howLongToType = 60.0 / wpm;

        for (String word : wordsToType) {
            try {
                Thread.sleep((long) (howLongToType * 1000));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
            this.addWordsTyped(word);
        }

        this.addWordsTyped("(Selesai)");

        typeRacer.getRareContestant().remove(this);
        int finishTime = (int) ((System.currentTimeMillis() - typeRacer.startTime) / 1000);
        typeRacer.addResult(new Result(this.botName, finishTime));
    }
}