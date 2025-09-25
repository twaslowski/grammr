import { Inflection } from '@/inflection/types/inflections';
import { Case, Feature, Number as Num, Person } from '@/types/feature';

export type FeatureIdentifier = Feature | Case | Num | Person;

export function getFormFromInflections(
  inflections: Inflection[],
  features: FeatureIdentifier[],
): string | undefined {
  const match = inflections.find((inf) =>
    features.every((desired) =>
      inf.features.some((f) => {
        if (typeof desired === 'object') {
          return f.value === desired.value || f.type === desired.type;
        }

        return f.value === desired || f.fullIdentifier === desired || f.type === desired;
      }),
    ),
  );

  return match?.inflected;
}
