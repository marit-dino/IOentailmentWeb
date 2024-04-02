export interface Response {
  entails: boolean,
  counterModelW?: Map<string, Map<string, Map<string, boolean>>>
  counterModelC?: Map<string, Map<string, boolean>>
}
