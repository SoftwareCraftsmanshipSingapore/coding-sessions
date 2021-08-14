from timer import Timer

class MockCounter:
    def count(self, number, notify=None):
        self.counts = number
        if notify:
            notify.updater(number)


def test_new_timer_should_not_be_set():
    timer = Timer()
    assert timer.state() == "not set"
    assert timer.remaining() == -999


def test_new_timer_can_be_set_to_10_seconds():
    timer = Timer()
    interval = 10
    timer.set(interval)
    assert timer.state() == "set"
    assert timer.remaining() == interval


def test_a_set_timer_can_be_started():
    timer = Timer()
    interval = 10
    timer.set(interval)
    timer.start()
    assert timer.state() == "running"


def test_new_timer_can_not_be_stated():
    timer = Timer()
    timer.start()
    assert timer.state() == "not set"


def test_a_running_timer_should_count_down():
    mockcounter = MockCounter()
    timer = Timer()
    timer.set_counter(mockcounter)
    timer.set(10)
    timer.start()
    mockcounter.count(2, timer) # wait_for_seconds(2)
    assert timer.remaining() == 8


def test_a_running_timer_should_stop_after_set_interval():
    mockcounter = MockCounter()
    timer = Timer()
    timer.set_counter(mockcounter)
    timer.set(10)
    timer.start()
    mockcounter.count(10, timer)
    assert timer.remaining() == 0
    assert timer.state() == "done"


def test_no_mocking():
    mockcounter = MockCounter()
    timer = Timer()
    timer.set_counter(mockcounter)
    timer.set(2)
    timer.start()
    timer.tick()
    timer.tick()
    assert timer.remaining() == 0
    assert timer.state() == "done"

