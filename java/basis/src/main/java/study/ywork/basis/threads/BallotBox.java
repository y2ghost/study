package study.ywork.basis.threads;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

class BallotBox implements Iterable<BallotPosition> {
    final BallotPosition[] data;

    @Override
    public Iterator<BallotPosition> iterator() {
        return Arrays.stream(data).toList().iterator();
    }

    BallotBox(List<String> list) {
        data = new BallotPosition[list.size()];
        for (int i = 0; i < list.size(); i++) {
            data[i] = new BallotPosition(list.get(i));
        }
    }

    public void voteFor(int i) {
        ++data[i].votes;
    }

    int getCandidateCount() {
        return data.length;
    }
}
