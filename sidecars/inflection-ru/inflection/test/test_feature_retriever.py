from inflection.service.feature_retriever import retrieve_features


def test_should_generate_verbal_features():
    part_of_speech = "VERB"
    features = retrieve_features(part_of_speech)
    # Python apparently cares about collection order,
    # so we sort the list and its contents recursively.
    sorted_features = sorted([sorted(feature) for feature in features])
    expected_features = sorted(
        [
            sorted({"1per", "sing"}),
            sorted({"2per", "sing"}),
            sorted({"3per", "sing"}),
            sorted({"1per", "plur"}),
            sorted({"2per", "plur"}),
            sorted({"3per", "plur"}),
        ]
    )
    assert sorted_features == expected_features


def test_should_generate_noun_features():
    part_of_speech = "NOUN"
    features = retrieve_features(part_of_speech)
    assert len(features) == 2 * 3 * 6
