package org.infra.domain;

class TestRoot extends AggregateRoot<Integer> {
    public void test() {
        this.apply(new TestEvent("Hassan"));
    }

    @ApplyAnnotation
    private void apply(TestEvent evt) {
        System.out.println(evt.hashCode());
    }

    static class TestEvent extends DomainEvent {
        public TestEvent(String name) {
            this.name = name;
        }

        public String name;
    }
}

class Main {
    public static void main(String[] args) {
        var sample = new TestRoot();
        var event = new TestRoot.TestEvent("Hassan");
        sample.loadFromHistory(event);

        System.out.println(sample.getVersion());
        System.out.println(sample.getUncommitted().size());
    }
}