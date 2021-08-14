class Timer:
    def __init__(self):
        self.interval = 0
        self._state = "not set"
        self.counter = None
        self.elapsed = 0

    def set(self, interval):
        self.interval = interval
        self._state = "set"

    def is_set(self):
        return self.interval > 0

    def remaining(self):
        delta = self.elapsed if self.counter else 0
        return self.interval - delta if self.is_set() else -999

    def state(self):
        return self._state

    def start(self):
        if self.is_set():
            self._state = "running"


    def set_counter(self, counter):
        self.counter = counter
        self.counter.callback = self.updater

    def tick(self):
        self.elapsed += 1
        if self.elapsed >= self.interval:
            self._state = "done"

    def updater(self, elapsed):
        self.elapsed = elapsed
        if self.elapsed >= self.interval:
            self._state = "done"
